import * as React from 'react';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';
import {FC} from "react";
import { useDropzone } from 'react-dropzone';
import CloudUploadTwoToneIcon from '@mui/icons-material/CloudUploadTwoTone';
import CloseTwoToneIcon from '@mui/icons-material/CloseTwoTone';
import CheckTwoToneIcon from '@mui/icons-material/CheckTwoTone';
import {
  Switch,
  styled,
  Grid,
  Box,
  Typography,
  Divider,
  FormControl,
  FormControlLabel,
  InputLabel,
  Select, Avatar,
} from '@mui/material';



const BoxUploadWrapper = styled(Box)(
    ({ theme }) => `
    height: 85px;
    border-radius: 15px;
    padding: ${theme.spacing(2)};
    background: ${theme.palette.grey["50"]};
    border: 1px dashed ${theme.palette.primary.main};
    outline: none;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    transition: ${theme.transitions.create(['border', 'background'])};

    &:hover {
      background: ${theme.palette.common.white};
      border-color: inherit;
    }
`
);
const AvatarDanger = styled(Avatar)(
    ({ theme }) => `
    background: ${theme.palette.error.light};
    width: ${theme.spacing(7)};
    height: ${theme.spacing(7)};
`
);
const AvatarWrapper = styled(Avatar)(
    ({ theme }) => `
    background: transparent;
    color: ${theme.palette.primary.main};
    width: ${theme.spacing(7)};
`
);

const AvatarSuccess = styled(Avatar)(
    ({ theme }) => `
    background: ${theme.palette.success.light};
    width: ${theme.spacing(7)};
    height: ${theme.spacing(7)};
`
);

export interface CreateDialogProps {
  open: boolean,
  onClose: () => void
}
const CreateDialog: FC<CreateDialogProps> = (props) => {
  const {
    isDragActive,
    isDragAccept,
    isDragReject,
    getRootProps,
    getInputProps
  } = useDropzone({
    accept: {
      'image/png': ['.png'],
    }
  });
  return (
      <div className="create-project-dialog">
        <Dialog open={props.open} onClose={() => props.onClose()}>
          <DialogTitle>Submit Your Content</DialogTitle>
          <DialogContentText sx={{p: 2}}>
            <Typography variant='body1' sx={{fontSize: '13px', p: 0}}>
              For a video project, submit a YouTube or Instagram Reels link. For an audio project,
              upload the file. We are in the process of adding support for external audio/podcast
              links and videos from other platforms.
            </Typography>

          </DialogContentText>
          <DialogContent>
            <Box p={0}>
              <FormControlLabel
                  control={<Switch color="warning" defaultChecked={false} />}
                  label={'Paraphrase'}
              />
            </Box>
            <Box p={0}>
              <FormControlLabel
                  disabled
                  control={<Switch color="warning" defaultChecked={false} />}
                  label={'Intelligently embed images (coming soon)'}
              />
            </Box>
            <Divider sx={{width: '100%'}}/>
            <Box p={3}>
              <Grid container spacing={3}>
                <Grid item xs={12}>
                  <TextField
                      className="create-dialog-textfield"
                      fullWidth
                      name="project_name"
                      variant="outlined"
                      label={('Project name')}
                      placeholder={('This is my video...')}
                  />
                </Grid>
                <Grid item xs={12}>
                  <FormControl fullWidth variant="outlined">
                    <InputLabel htmlFor="content_type">{('Content Type')}</InputLabel>
                    <Select
                        required
                        native
                        label={('Content Type')}
                        inputProps={{
                          name: 'content_type'
                        }}
                    >
                      <option aria-label="None" value="" />
                      <option value={0}>{('Food Recipe')}</option>
                      <option value={1}>{('Interview')}</option>
                      <option value={2}>{('Podcast')}</option>
                      <option value={3}>{('Lecture')}</option>
                      <option value={4}>{('DIY')}</option>
                      <option value={5}>{('Other')}</option>
                    </Select>
                  </FormControl>
                </Grid>
                <Grid item xs={12}>
                  <TextField
                      className="create-dialog-textfield"
                      fullWidth
                      name="content_link"
                      variant="outlined"
                      label={('Content Link')}
                      // https://www.youtube.com/watch?v=91BUM3WhCfo&t=6s
                      placeholder={('https://www.youtube.com/watch?v=91BUM...')}
                  />
                </Grid>
              </Grid>
            </Box>
            <Divider>OR</Divider>
            <Box p={2}>
              <BoxUploadWrapper {...getRootProps()}>
                <input {...getInputProps()} />
                {isDragAccept && (
                    <>
                      <AvatarSuccess variant="rounded">
                        <CheckTwoToneIcon />
                      </AvatarSuccess>
                      <Typography
                          sx={{
                            mt: 2
                          }}
                      >
                        {('Drop the files to start uploading')}
                      </Typography>
                    </>
                )}
                {isDragReject && (
                    <>
                      <AvatarDanger variant="rounded">
                        <CloseTwoToneIcon />
                      </AvatarDanger>
                      <Typography
                          sx={{
                            mt: 2
                          }}
                      >
                        {('You cannot upload these file types')}
                      </Typography>
                    </>
                )}
                {!isDragActive && (
                    <>
                      <AvatarWrapper variant="rounded">
                        <CloudUploadTwoToneIcon />
                      </AvatarWrapper>
                      <Typography
                          sx={{
                            mt: 1
                          }}
                      >
                        {('Drag & drop files here')}
                      </Typography>
                    </>
                )}
              </BoxUploadWrapper>
            </Box>
          </DialogContent>
          <DialogActions>
            <Button onClick={() => props.onClose()} variant={'contained'} color={'error'}>Cancel</Button>
            <Button onClick={() => props.onClose()} variant={'contained'} color={'success'}>Submit</Button>
          </DialogActions>
        </Dialog>
      </div>
  );
}
export default CreateDialog