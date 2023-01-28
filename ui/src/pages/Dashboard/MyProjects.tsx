import React, {FC, useState} from 'react';
import {observer} from "mobx-react";
import {Box, Button, Grid, Typography} from "@mui/material";
import AddIcon from '@mui/icons-material/Add';
import DeleteIcon from '@mui/icons-material/Delete';
import ConfirmActionDialog from "@/pages/Dashboard/ConfirmActionDialog";
import {useRouter} from "next/router";
import {PATHS} from "@/utils/constants";
import PendingJobs from "@/pages/Dashboard/PendingJobs";

export interface MyProjectsProps {

}

const MyProjects: FC<MyProjectsProps> = observer((_props) => {
  const router = useRouter()
  const [dialogOpen, setDialogOpen] = useState<boolean>(false)

  const onCreate = () => {
    // console.log("DEBUG --- onCreate clicked!!")
  }

  const onOpenProject = () => {
    router.push({
      pathname: PATHS.PROJECT + '/' + 17
    }, undefined, {shallow: true})
    .catch(e => console.log(e))
  }
  const onDeleteProject = () => {
    console.log("DEBUG: onDeleteProject")
    setDialogOpen(true)
  }

  const onCloseDialog = () => {
    setDialogOpen(false)
  }

  const abridgedTitle = (title: string) => {
    const maxLength = 42
    if (title && title.length > maxLength) {
      const prefix = title.slice(0, maxLength)
      return prefix + '...'
    }
    return title;
  }

  return (
      <Grid container className="dashboard-container">
        <Grid item sm={12} md={2} sx={{width: '100%'}}>
          <Box className="create-project-btn-box" onClick={onCreate}>
            <Button
                sx={{width: '30px'}}
                color="inherit"
                startIcon={<AddIcon/>}
                className="create-project-btn"
                size='large'
                variant='text'
                onClick={onCreate}
            />
            <Typography variant='subtitle2' sx={{mt: 7, textAlign: 'center'}}>Create a new project</Typography>
          </Box>
        </Grid>
        <Grid item sm={12} md={10} sx={{width: '100%'}}>
          <Box className="dashboard-pending-jobs">
            <PendingJobs/>
          </Box>
        </Grid>
        <Grid item sm={12} className="files-section">
          <h3>Your Files</h3>
        </Grid>
        <Grid item sx={{p: 2}}>
          <ConfirmActionDialog open={dialogOpen} onCloseDialog={onCloseDialog}/>
          <div className="my-files-folder blue">
            <Button
                sx={{width: '20px'}}
                color="error"
                startIcon={<DeleteIcon/>}
                className="folder-delete-btn"
                size='small'
                variant='text'
                onClick={onDeleteProject}
            />
            <h6 onClick={onOpenProject}
                className="folder-title">{abridgedTitle("What is the meaning of life without one's loved")}</h6>
          </div>
          <div className="my-files-folder red">
          </div>
          <div className="my-files-folder yellow">
          </div>
          <div className="my-files-folder green">
          </div>
        </Grid>
      </Grid>
  );
})

export default MyProjects;
