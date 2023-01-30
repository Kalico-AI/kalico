import React, {FC, useCallback, useMemo, useState} from 'react'
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
import {ContentItem, ProjectApi, ProjectDetail} from "@/api";
import {AuthUserContext} from "next-firebase-auth";
import {headerConfig} from "@/api/headerConfig";
import {debounce} from "lodash";

export interface EditorProps {
  project: ProjectDetail,
  user: AuthUserContext,
}

const ForcedLayoutEditor: FC<EditorProps> = (props) => {
  const renderElement = useCallback(props => <EditorElement {...props} />, [])
  const [_content, setContent] = useState<Descendant[]>([])

  const editor = useMemo(
      () => withImages(withLayout(withHistory(withReact(createEditor())))),
      []
  )

  const throttleSave = useCallback(
      debounce(nextValue => saveToDb(nextValue), 5000),
      [],
  );

  const saveToDb = (content: Descendant[]) => {
    props.user?.getIdToken(false)
    .then(tokenResult => {
      const projectApi = new ProjectApi(headerConfig(tokenResult))
      projectApi.updateProjectContent({
        id: props.project.id,
        content: content as ContentItem[]
      })
      .then(_ => {
      }).catch(e => console.log(e))
    }).catch(e => console.log(e))
  }

  const handleChange = (content: Descendant[]) => {
    setContent(content)
    throttleSave(content);
  }
  return (
      <Box className="slate-editor-box">
      <Slate editor={editor} value={props.project.content as Descendant[]} onChange={handleChange}>
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