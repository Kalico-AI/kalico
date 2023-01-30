import React, {FC, useCallback, useMemo} from 'react'
import {Slate, Editable, withReact} from 'slate-react'
import {
  createEditor,
} from 'slate'
import { withHistory } from 'slate-history'

import {Box} from "@mui/material";
import {withImages, withLayout} from "@/components/SlateEditor/utils";
import {ProjectDetail} from "@/api";
import {AuthUserContext} from "next-firebase-auth";

export interface EditorProps {
  project: ProjectDetail,
  user: AuthUserContext
}

const ForcedLayoutEditor: FC<EditorProps> = (props) => {
  const renderElement = useCallback(props => <Element {...props} />, [])
  const editor = useMemo(
      () => withImages(withLayout(withHistory(withReact(createEditor())))),
      []
  )
  return (
      <Box className="slate-editor-box">
      <Slate editor={editor} value={props.project.content as any}>
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

// const initialValue: Descendant[] = [
//   {
//     type: 'title',
//     children: [{ text: 'Untitled' }],
//   },
//   {
//     type: 'paragraph',
//     children: [
//       {
//         text:
//             'Enter your body text here',
//       },
//     ],
//   },
//   {
//     type: 'heading',
//     children: [
//       {
//         text:
//             'This is a subheading!',
//       },
//     ],
//   },
//   {
//     type: 'image',
//     url: 'https://source.unsplash.com/zOwZKwZOZq8',
//     children: [{ text: '' }],
//   },
//   {
//     type: 'paragraph',
//     children: [
//       {
//         text:
//             'Enter your body text here',
//       },
//     ],
//   },
//   {
//     type: 'image',
//     url: 'https://source.unsplash.com/zOwZKwZOZq8',
//     children: [{ text: '' }],
//   },
// ]

export default ForcedLayoutEditor