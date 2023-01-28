import React, { useCallback, useMemo } from 'react'
import {Slate, Editable, withReact, useSlateStatic} from 'slate-react'
import isUrl from 'is-url'
import {
  Transforms,
  createEditor,
  Node,
  Element as SlateElement,
  Descendant,
  Editor,
} from 'slate'
import { withHistory } from 'slate-history'
import {ImageElement, ParagraphElement, TitleElement} from "@/components/SlateEditor/custom-types";
import {Box, Button, Icon} from "@mui/material";
import imageExtensions from 'image-extensions'

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

const insertImage = (editor, url) => {
  const text = { text: '' }
  const image: ImageElement = { type: 'image', url, children: [text] }
  Transforms.insertNodes(editor, image)
}

const isImageUrl = url => {
  if (!url) return false
  if (!isUrl(url)) return false
  const ext = new URL(url).pathname.split('.').pop()
  return imageExtensions.includes(ext)
}

// @ts-ignore
const InsertImageButton = () => {
  const editor = useSlateStatic()
  return (
      <Button
          onMouseDown={event => {
            event.preventDefault()
            const url = window.prompt('Enter the URL of the image:')
            if (url && !isImageUrl(url)) {
              alert('URL is not an image')
              return
            }
            url && insertImage(editor, url)
          }}
      >
        <Icon>image</Icon>
      </Button>
  )
}

const withImages = editor => {
  const { insertData, isVoid } = editor

  editor.isVoid = element => {
    return element.type === 'image' ? true : isVoid(element)
  }

  editor.insertData = data => {
    const text = data.getData('text/plain')
    const { files } = data

    if (files && files.length > 0) {
      for (const file of files) {
        const reader = new FileReader()
        const [mime] = file.type.split('/')

        if (mime === 'image') {
          reader.addEventListener('load', () => {
            const url = reader.result
            insertImage(editor, url)
          })

          reader.readAsDataURL(file)
        }
      }
    } else if (isImageUrl(text)) {
      insertImage(editor, text)
    } else {
      insertData(data)
    }
  }

  return editor
}

const ForcedLayoutEditor = () => {
  const renderElement = useCallback(props => <Element {...props} />, [])
  const editor = useMemo(
      () => withImages(withLayout(withHistory(withReact(createEditor())))),
      []
  )
  return (
      <Box className="slate-editor-box">
      <Slate editor={editor} value={initialValue}>
        {/*<Toolbar>*/}
        {/*  <InsertImageButton />*/}
        {/*</Toolbar>*/}
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

const Element = (props) => {
  const { attributes, children, element } = props
  switch (element.type) {
    case 'title':
      return <h3 {...attributes} className="slate-editor-title">{children}</h3>
    case 'heading':
      return <h5 {...attributes} className="slate-editor-heading">{children}</h5>
    case 'paragraph':
      return <p {...attributes} className="slate-editor-paragraph">{children}</p>
    case 'image':
      return <img {...attributes} alt={''} src={element.url} className="slate-editor-img"/>
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
  {
    type: 'heading',
    children: [
      {
        text:
            'This is a subheading!',
      },
    ],
  },
  {
    type: 'image',
    url: 'https://source.unsplash.com/zOwZKwZOZq8',
    children: [{ text: '' }],
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
  {
    type: 'image',
    url: 'https://source.unsplash.com/zOwZKwZOZq8',
    children: [{ text: '' }],
  },
]

export default ForcedLayoutEditor