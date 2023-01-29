import React, {FC, useEffect, useState} from 'react';
import {observer} from "mobx-react";
import {Box, Button, Grid, Typography} from "@mui/material";
import AddIcon from '@mui/icons-material/Add';
import DeleteIcon from '@mui/icons-material/Delete';
import ConfirmActionDialog from "@/pages/Dashboard/ConfirmActionDialog";
import {useRouter} from "next/router";
import {PATHS} from "@/utils/constants";
import PendingJobs from "@/pages/Dashboard/PendingJobs";
import CreateDialog from "@/pages/Dashboard/CreateDialog";
import {toast, ToastContainer} from "react-toastify";

export interface MyProjectsProps {

}

interface Project {
  name: string,
  id: number
}

const folderColors = {
  BLUE: 'blue',
  RED: 'red',
  GREEN: 'green',
  YELLOW: 'yellow'
}

const MyProjects: FC<MyProjectsProps> = observer((_props) => {
  const router = useRouter()
  const [deleteDialogOpen, setDeleteDialogOpen] = useState<boolean>(false)
  const [createDialogOpen, setCreateDialogOpen] = useState<boolean>(false)

  const onCreate = () => {
    setCreateDialogOpen(true)
  }

  const onSubmitProject = () => {
    setCreateDialogOpen(false)
    // toast("Your project is now being processed", {
    //   type: 'success',
    //   position: toast.POSITION.TOP_CENTER
    // });
    const resolveAfter3Sec = new Promise(resolve => setTimeout(resolve, 3000));
    toast.promise(
        resolveAfter3Sec,
        {
          pending: 'Uploading file',
          success: 'Your file has been uploaded and processing has begun',
          error: 'Something went wrong while uploading your file'
        }
    )
  }

  const onOpenProject = () => {
    router.push({
      pathname: PATHS.PROJECT + '/' + 17
    }, undefined, {shallow: true})
    .catch(e => console.log(e))
  }
  const onDeleteProject = () => {
    setDeleteDialogOpen(true)
  }

  const onCloseDeleteDialog = () => {
    setDeleteDialogOpen(false)
  }

  const onCloseCreateDialog = () => {
    setCreateDialogOpen(false)
  }

  const abridgedTitle = (title: string) => {
    const maxLength = 62
    if (title && title.length > maxLength) {
      const prefix = title.slice(0, maxLength)
      return prefix + '...'
    }
    return title;
  }

  const getProjects = (): Project[] => {
    const projects = []
    const count = 12
    projects.push(
        {
          name: 'My demo project',
          id: 0
        }
    )
    for (let i = 0; i < count; i++) {
      projects.push(
          {
            name: i + 1 + ": What is the meaning of life without one's loved",
            id: i + 1
          }
      )
    }
    return projects
  }

  const getProjectComponents = (projects: Project[]) => {
    let prevClassName = ''
    return projects.map((item, index) => {
      const folderClass = getFolderClassName(prevClassName)
      prevClassName = folderClass
      return (
          <Grid item sx={{p: 2}} sm={3} key={index}>
            <div className={folderClass}>
              <Button
                  sx={{width: '30px'}}
                  color="warning"
                  startIcon={<DeleteIcon/>}
                  className="folder-delete-btn"
                  size='large'
                  variant='text'
                  onClick={onDeleteProject}
              />
              <h6 onClick={onOpenProject}
                  className="folder-title">{abridgedTitle(item.name)}</h6>
            </div>
          </Grid>
      )
    })
  }

  const getFolderClassName = (prevColor: string): string => {
    // Get sequential colors as follows: blue -> red -> green -> yellow
    const name = "my-files-folder "
    let nextColor = folderColors.BLUE
    if (prevColor) {
      if (prevColor.includes(folderColors.BLUE)) {
        nextColor = folderColors.RED
      } else  if (prevColor.includes(folderColors.RED)) {
        nextColor = folderColors.GREEN
      } else  if (prevColor.includes(folderColors.GREEN)) {
        nextColor = folderColors.YELLOW
      } else  if (prevColor.includes(folderColors.YELLOW)) {
        nextColor = folderColors.BLUE
      }
    }
    return name + nextColor
  }

  useEffect(() => {

  }, [])

  return (
      <>
      <Grid container className="dashboard-container">
        <Grid item sm={12} md={6}>
          <CreateDialog open={createDialogOpen}
                        onSubmit={onSubmitProject}
                        onClose={onCloseCreateDialog}/>
          <Box className="create-project-btn-box" onClick={onCreate}>
            <Button
                sx={{width: '30px'}}
                color="inherit"
                startIcon={<AddIcon/>}
                className="create-project-btn"
                size='large'
                variant='text'
            />
            <Typography variant='subtitle2' sx={{mt: 7, textAlign: 'center', p: 1}}>Create a new project</Typography>
          </Box>
        </Grid>
        <Grid item sm={12} md={6} justifyContent='flex-end' sx={{display: 'flex'}}>
          <Box className="dashboard-pending-jobs">
            <PendingJobs/>
          </Box>
        </Grid>
        <Grid item sm={12} className="files-section">
          <h3>Your Files</h3>
        </Grid>
        {
          getProjectComponents(getProjects())
        }
      </Grid>
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
        <ConfirmActionDialog open={deleteDialogOpen} onCloseDialog={onCloseDeleteDialog}/>
        </>
  );
})

export default MyProjects;
