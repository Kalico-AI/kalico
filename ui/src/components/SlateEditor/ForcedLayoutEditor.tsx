import React, {
  FC,
  RefObject,
  useCallback,
  useEffect,
  useMemo,
  useRef, useState,
} from 'react'
import {
  Slate,
  Editable,
  withReact
} from 'slate-react'
import {
  createEditor, Descendant,
} from 'slate'
import { withHistory } from 'slate-history'
import {Box, Button, Grid} from "@mui/material";
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
import SaveIcon from '@mui/icons-material/Save';
import ContentCopyIcon from '@mui/icons-material/ContentCopy';
import PictureAsPdfIcon from '@mui/icons-material/PictureAsPdf';
import {toast, ToastContainer} from "react-toastify";
import { pdf, PDFViewer } from '@react-pdf/renderer';
import { saveAs } from 'file-saver';
import PDFGenerator from "@/components/SlateEditor/PDFGenerator";


export interface EditorProps {
  project: ProjectDetail,
  user: AuthUserContext,
  setEditorRef: (ref: RefObject<HTMLElement>) => void,
  editable?: boolean
}

const initialValue: { children: { text: string }[]; type: string }[] = [
  {
    type: 'title',
    children: [
      {
        text:
            "Untitled",
      },
    ],
  },
]


const ForcedLayoutEditor: FC<EditorProps> = (props) => {
  const renderElement = useCallback(props => <EditorElement {...props} />, [])
  const editorRef = useRef<HTMLElement>()
  const [content, setContent] = useState<Descendant[]>([])
  const [contentSaved, setContentSaved] = useState<boolean>(false)

  const editor = useMemo(
      () => withImages(withLayout(withHistory(withReact(createEditor())))),
      []
  )

  const throttleSave = useCallback(
      debounce((value: Descendant[]) => saveToDb(value), 3000),
      [],
  );


  const copyToClipboard = () => {
    const element = document.getElementById("editor-content")
    if (typeof window !== 'undefined' && navigator.clipboard) {
      navigator.clipboard.writeText(element?.innerText).then(
          () => {
            toast("Project copied to clipboard", {
              type: 'success',
              position: toast.POSITION.TOP_CENTER
            });
          },
          () => {
            toast("Failed to copy project", {
              type: 'error',
              position: toast.POSITION.TOP_CENTER
            });
          }
      ).catch(e => console.log(e));
    }
  }


  const exportPdf = async () => {
    const blob = await pdf((<PDFGenerator content={contentSaved ? content : props.project.content as Descendant[]}/>))
    .toBlob();
    saveAs(blob, props.project.name + '.pdf');
  };

  useEffect(() => {
    props.setEditorRef(editorRef)
  }, [editorRef !== undefined])

  const quickSaveToDb = () => {
    saveToDb(contentSaved ? content : props.project.content as Descendant[])
  }


  const saveToDb = (content: Descendant[]) => {
    if (props.editable) {
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
  }

  const handleChange = (content: Descendant[]) => {
    setContent(content)
    setContentSaved(true)
    throttleSave(content);
  }
  // const isPdf = true
  // if (isPdf) {
  //   return (
  //       <div style={{
  //         width: '100%'
  //       }}>
  //       <PDFViewer>
  //         {/*<PDFGenerator content={props.project.content ? props.project.content as Descendant[] : initialValue}/>*/}
  //         {/*<PDFGenerator/>*/}
  //       </PDFViewer>
  //       </div>
  //   )
  // }
  return (
      <Box className="slate-editor-box"  ref={editorRef}>
        <ToastContainer
            style={{width: '100%', maxWidth: '600px'}}
            position="top-center"
            autoClose={5000}
            hideProgressBar
            newestOnTop={false}
            closeOnClick
            rtl={false}
            pauseOnFocusLoss
            draggable
            pauseOnHover
            theme="colored"/>
      <Slate
          editor={editor}
             value={props.project.content ? props.project.content as Descendant[] : initialValue}
             onChange={handleChange}>
          {/*<Toolbar>*/}
          {/*  <InsertImageButton />*/}
          {/*</Toolbar>*/}
        <Editable
            id="editor-content"
            renderElement={renderElement}
            placeholder="Untitled"
            spellCheck
            autoFocus
        />
      </Slate>
        <Box className="slate-editor-sticky-controls">
         <Grid container sx={{maxWidth: '400px', margin: '0 auto'}}>
           <Grid item sm={6}>
             <Button
                 color="success"
                 startIcon={<SaveIcon/>}
                 className="upgrade-button"
                 size='large'
                 variant='text'
                 onClick={quickSaveToDb}
             >Save</Button>
           </Grid>
           {/*<Grid item sm={4}>*/}
           {/*  <Button*/}
           {/*      color="warning"*/}
           {/*      startIcon={<PictureAsPdfIcon/>}*/}
           {/*      className="upgrade-button"*/}
           {/*      size='large'*/}
           {/*      variant='text'*/}
           {/*      onClick={exportPdf}*/}
           {/*  >Export</Button>*/}
           {/*</Grid>*/}
           <Grid item sm={6}>
             <Button
                 color="secondary"
                 startIcon={<ContentCopyIcon/>}
                 className="upgrade-button"
                 size='large'
                 variant='text'
                 onClick={copyToClipboard}
             >Copy</Button>
           </Grid>
         </Grid>
        </Box>
      </Box>
  )
}

export default ForcedLayoutEditor