import {
  Card,
  Box,
  Typography,
  Avatar,
  LinearProgress,
  styled
} from '@mui/material';
import CloudSyncIcon from '@mui/icons-material/CloudSync';
import {FC, useEffect, useState} from "react";
import {Project, ProjectApi} from "@/api";
import {AuthUserContext} from "next-firebase-auth";
import {headerConfig} from "@/api/headerConfig";

const AvatarWrapperError = styled(Avatar)(
  ({ theme }) => `
      width: ${theme.spacing(2)};
      height: ${theme.spacing(2)};
      border-radius: 15px;
      background-color: #fff;
      color:  red;
      margin-bottom: ${theme.spacing(2)};
`
);

export interface PendingJobsProps {
  project?: Project,
  user?: AuthUserContext,
  onNewProjectCreated: (projectId: number) => void
}
const PendingJobs: FC<PendingJobsProps> = (props) => {
  const [percent, setPercent] = useState(0)
  const [estimatedTime, setEstimatedTime] = useState('')
  const [progressMessage, setProgressMessage] = useState('Processing')

  useEffect(() => {
    const interval = setInterval(() => {
      getProgress()
    }, 2000)

    return () => {
      clearInterval(interval)
    }
  }, [props.project])

  const getProgress = () => {
    props?.user?.getIdToken(false)
    .then(tokenResult => {
      const projectApi = new ProjectApi(headerConfig(tokenResult))
      projectApi.getProjectJobStatus(props?.project?.id)
      .then(response => {
        if (response.data) {
          setPercent(response.data.percent_complete)
          setEstimatedTime(response.data.estimated_time)
          console.log("data: ", response.data.percent_complete)
          if (response.data.percent_complete === 100) {
            setProgressMessage('Processed')
            props.onNewProjectCreated(props.project.id)
          } else {
            setProgressMessage('Processing')
          }
        }
      }).catch(e => console.log(e))
    }).catch(e => console.log(e))
  }

  useEffect(() => {

  }, [props.project])

  return (
    <Card
      sx={{
        p: 2,
        m: 2,
        height: '185px',
        border: '3px solid orange',
        borderRadius: '15px',
        boxShadow: 'none'
      }}
    >
      <AvatarWrapperError>
        <CloudSyncIcon />
      </AvatarWrapperError>
      {props.project ? <>
        <Typography
            variant="body1"
            sx={{
              pb: 1,
              display: 'inline-flex'
            }}
        >
          {progressMessage}
        </Typography>
        {' '}
        <Typography
            variant="body1"
            sx={{
              color: 'orange',
              display: 'inline-flex'
            }}
        >
          <strong>{props.project.project_name}</strong>
          {/*<Typography*/}
          {/*    color="text.primary"*/}
          {/*    variant="h4"*/}
          {/*    sx={{*/}
          {/*      pr: 0.5,*/}
          {/*      display: 'block'*/}
          {/*    }}*/}
          {/*>*/}
          {/*  {percent}%*/}
          {/*</Typography>*/}
        </Typography>
            <Box pt={2}>
              <LinearProgress value={percent} color="error" variant="determinate" />
            </Box>
            <Box>
              <Typography sx={{fontSize: '12px', pt: 1}}>
                Estimated time: {estimatedTime}
              </Typography>
            </Box>
      </> :
          <Typography
              variant="body1"
              sx={{
                pb: 1,
                display: 'inline-flex'
              }}
          >
            No pending jobs
          </Typography>
      }
    </Card>
  );
}

export default PendingJobs;
