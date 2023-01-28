import React, { useCallback, useMemo } from 'react'
import { Slate, Editable, withReact } from 'slate-react'
import {
  Transforms,
  createEditor,
  Node,
  Element as SlateElement,
  Descendant,
  Editor,
} from 'slate'
import { withHistory } from 'slate-history'
import {ParagraphElement, TitleElement} from "@/components/SlateEditor/custom-types";
import {Box} from "@mui/material";

const withLayout = editor => {
  const { normalizeNode } = editor

  editor.normalizeNode = ([node, path]) => {
    if (path.length === 0) {
      if (editor.children.length <= 1 && Editor.string(editor, [0, 0]) === '') {
        const title: TitleElement = {
          type: 'title',
          children: [{ text: 'Untitled' }],
        }
        Transforms.insertNodes(editor, title, {
          at: path.concat(0),
          select: true,
        })
      }

      if (editor.children.length < 2) {
        const paragraph: ParagraphElement = {
          type: 'paragraph',
          children: [{ text: '' }],
        }
        Transforms.insertNodes(editor, paragraph, { at: path.concat(1) })
      }

      for (const [child, childPath] of Node.children(editor, path)) {
        let type: string
        const slateIndex = childPath[0]
        const enforceType = type => {
          if (SlateElement.isElement(child) && child.type !== type) {
            const newProperties: Partial<SlateElement> = { type }
            Transforms.setNodes<SlateElement>(editor, newProperties, {
              at: childPath,
            })
          }
        }

        switch (slateIndex) {
          case 0:
            type = 'title'
            enforceType(type)
            break
          case 1:
            type = 'paragraph'
            enforceType(type)
            break
          default:
            break
        }
      }
    }

    return normalizeNode([node, path])
  }

  return editor
}

const ForcedLayoutEditor = () => {
  const renderElement = useCallback(props => <Element {...props} />, [])
  const editor = useMemo(
      () => withLayout(withHistory(withReact(createEditor()))),
      []
  )
  return (
      <Box className="slate-editor-box">
      <Slate editor={editor} value={initialValue}>
        <Editable
            renderElement={renderElement}
            placeholder="Untitled"
            spellCheck
            autoFocus
        />
      </Slate>
      </Box>
  )
}

const Element = ({ attributes, children, element }) => {
  switch (element.type) {
    case 'title':
      return <h3 {...attributes}>{children}</h3>
    case 'paragraph':
      return <p {...attributes}>{children}</p>
  }
}

const initialValue: Descendant[] = [
  {
    type: 'title',
    children: [{ text: 'Untitled' }],
  },
  {
    type: 'paragraph',
    children: [
      {
        text:
            'Enter your body text here',
      },
    ],
  },
]

export default ForcedLayoutEditor