import React, {FC, useCallback, useMemo} from 'react'
import {
  Slate,
  Editable,
  withReact
} from 'slate-react'
import {
  createEditor, Descendant,
} from 'slate'
import { withHistory } from 'slate-history'
import {Box} from "@mui/material";
import {
  EditorElement,
  InsertImageButton,
  Toolbar,
  withImages,
  withLayout
} from "@/components/SlateEditor/utils";
import {ProjectDetail} from "@/api";
import {AuthUserContext} from "next-firebase-auth";

export interface EditorProps {
  project: ProjectDetail,
  user: AuthUserContext
}

const ForcedLayoutEditor: FC<EditorProps> = (props) => {
  const renderElement = useCallback(props => <EditorElement {...props} />, [])
  const editor = useMemo(
      () => withImages(withLayout(withHistory(withReact(createEditor())))),
      []
  )
  return (
      <Box className="slate-editor-box">
      <Slate editor={editor} value={props.project.content as Descendant[]}>
          <Toolbar>
            <InsertImageButton />
          </Toolbar>
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

export default ForcedLayoutEditor