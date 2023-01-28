import {
  Card,
  Box,
  Typography,
  Avatar,
  LinearProgress,
  styled
} from '@mui/material';
import CloudSyncIcon from '@mui/icons-material/CloudSync';

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

function PendingJobs() {

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
      <Typography
        variant="body1"
        sx={{
          pb: 1,
          display: 'inline-flex'
        }}
      >
        Processing project
      </Typography>
      {' '}
      <Typography
          variant="body1"
          sx={{
            color: 'orange',
            display: 'inline-flex'
          }}
      >
        <strong>Hello</strong>
      </Typography>
      <Typography
        color="text.primary"
        variant="h4"
        sx={{
          pr: 0.5,
          display: 'block'
        }}
      >
        50%
      </Typography>
      <Box pt={2}>
        <LinearProgress value={50} color="error" variant="determinate" />
      </Box>
    </Card>
  );
}

export default PendingJobs;
