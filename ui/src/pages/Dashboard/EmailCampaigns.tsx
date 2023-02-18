import React, {FC, useState} from "react";
import Grid from "@mui/material/Grid";
import Card from "@mui/material/Card";
import CardHeader from "@mui/material/CardHeader";
import CardContent from "@mui/material/CardContent";
import Typography from "@mui/material/Typography";
import TextField from "@mui/material/TextField";
import CardActions from "@mui/material/CardActions";
import Button from "@mui/material/Button";
import Box from "@mui/material/Box";
import {
  CardActionArea,
  CircularProgress,
  FormControlLabel,
  Radio,
  RadioGroup,
  TextareaAutosize
} from "@mui/material";
import {headerConfig} from "@/api/headerConfig";
import Tabs from "@mui/material/Tabs";
import Tab from "@mui/material/Tab";
import {EmailCampaign, LeadApi} from "@/api";
import {AuthUserContext} from "next-firebase-auth";
import {toast, ToastContainer} from "react-toastify";
import {getFormattedDate} from "@/utils/utils";
interface TabPanelProps {
  children?: React.ReactNode;
  index: number;
  value: number;
}

function TabPanel(props: TabPanelProps) {
  const { children, value, index, ...other } = props;

  return (
      <div
          role="tabpanel"
          hidden={value !== index}
          id={`simple-tabpanel-${index}`}
          aria-labelledby={`simple-tab-${index}`}
          {...other}
      >
        {value === index && (
            <Box sx={{ p: 3 }}>
              <Typography>{children}</Typography>
            </Box>
        )}
      </div>
  );
}

function a11yProps(index: number) {
  return {
    id: `simple-tab-${index}`,
    'aria-controls': `simple-tabpanel-${index}`,
  };
}


export interface EmailCampaignProps {
  campaigns?: EmailCampaign[]
  user: AuthUserContext
}


const EmailCampaigns: FC<EmailCampaignProps> = (props) => {
  const [tabIndex, setTabIndex] = React.useState(0);
  const [subject, setSubject] = useState<string>('')
  const [template, setTemplate] = useState<string>('')
  const [numEmailsSent, setNumEmailsSent] = useState<number>(0)
  const [personalizedByName, setPersonalizedByName] = useState<boolean>(false)
  const [personalizedByOther, setPersonalizedByOther] = useState<boolean>(true)

  const handleTabChange = (_event: React.SyntheticEvent, newValue: number) => {
    setTabIndex(newValue);
  };

  const handleTemplateChange = (e: any) => {
    e.preventDefault()
    setTemplate(e.target.value)
  }

  const handleSubjectChange = (e: any) => {
    e.preventDefault()
    setSubject(e.target.value)
  }

  const handleNumEmailsSentChange = (e: any) => {
    e.preventDefault()
    setNumEmailsSent(e.target.value)
  }

  const handleRadioChange = (e: any) => {
    e.preventDefault()
    const value = e.target.value
    if (value === 'by_name') {
      setPersonalizedByName(true)
      setPersonalizedByOther(false)
    } else {
      setPersonalizedByName(false)
      setPersonalizedByOther(true)
    }
  }


  const handleCreateCampaign = () => {
    props.user.getIdToken(false)
    .then(tokenResult => {
      const leadApi = new LeadApi(headerConfig(tokenResult))
      leadApi.createEmailCampaign({
        subject: subject,
        template: template,
        num_emails_sent: numEmailsSent,
        personalized_by_name: personalizedByName,
        personalized_by_other: personalizedByOther
      })
      .then(_ => {
        toast("Campaign Created", {
          type: 'success',
          position: toast.POSITION.TOP_CENTER
        });
      }).catch(e => console.log(e))
    }).catch(e => console.log(e))
  }


  return (
      <Grid container sx={{pt: 15}}  justifyContent='center' alignContent='center'>
        <Grid item sm={12}>
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
        </Grid>
        <Grid item sm={12} alignItems='center'>
          {props.campaigns ?
              <>
                  <Box>
                    <Tabs value={tabIndex} onChange={handleTabChange}>
                      <Tab label="View Campaigns" {...a11yProps(0)} />
                      <Tab label="Create Campaign" {...a11yProps(1)} />
                    </Tabs>
                  </Box>
                  <TabPanel value={tabIndex} index={0}>
                    {props?.campaigns && props?.campaigns.map(it =>
                        <Card sx={{ mb: 2 }} key={it.campaign_id}>
                          <CardActionArea>
                            <CardContent>
                              <Typography gutterBottom variant="h5" component="div">
                                Subject: {it.subject}
                              </Typography>
                              <Box>
                                <Typography>
                                  Campaign ID: {it.campaign_id}
                                </Typography>
                                <Typography>
                                  Date Created: {getFormattedDate(it.date_created)}
                                </Typography>
                                <Typography>
                                </Typography>
                                <Typography>
                                  Personalized by Name: {it.personalized_by_name ? 'true' : 'false'}
                                </Typography>
                                <Typography>
                                  Personalized by Other: {it.personalized_by_other ? 'true': 'false'}
                                </Typography>
                                <Typography>
                                  Number of Emails Sent: {it.num_emails_sent}
                                </Typography>
                                <Typography variant="h5" color="orange">
                                  Open Rate: {it.open_rate}%
                                </Typography>
                              </Box>
                            </CardContent>
                          </CardActionArea>
                        </Card>
                    )}
                  </TabPanel>
                  <TabPanel value={tabIndex} index={1}>
                    <Card sx={{maxWidth: '650px', margin: '0 auto', mt: 5}}>
                      <CardHeader  titleTypographyProps={{ variant: 'h6' }} />
                      <CardContent style={{padding: '0rem', marginLeft: '0rem'}}>
                        <Grid container spacing={5}>
                          <Grid item xs={12} >
                            <Grid container spacing={5} sx={{p: 4}}>
                              <Grid item xs={12} sm={12}>
                                <TextField
                                    onChange={handleSubjectChange}
                                    fullWidth
                                    label='Email Subject'
                                    placeholder='Email Subject'
                                    value={subject} />
                              </Grid>
                              <Grid item xs={12} sm={12}>
                                <TextField
                                    onChange={handleNumEmailsSentChange}
                                    label='Number of Emails to be Sent'
                                    type="number"
                                    placeholder='0'
                                    value={numEmailsSent} />
                              </Grid>
                              <Grid item xs={12} sm={12}>
                                <RadioGroup
                                    aria-labelledby="dradio-buttons-group-label"
                                    defaultValue="by_other"
                                    name="radio-buttons-group"
                                    onChange={handleRadioChange}
                                >
                                  <FormControlLabel value="by_other" control={<Radio />} label="Customized by Other" />
                                  <FormControlLabel value="by_name" control={<Radio />} label="Customized by Name" />
                                </RadioGroup>
                              </Grid>
                              <Grid item xs={12}>
                                <Box >
                                  <TextareaAutosize
                                      style={{paddingTop: '10px'}}
                                      minRows={10}
                                      onChange={handleTemplateChange}
                                      placeholder='Create a template in Gmail and paste it here'
                                      value={template} />
                                </Box>
                              </Grid>
                            </Grid>
                          </Grid>
                        </Grid>
                      </CardContent>
                      <CardActions>
                        <Button
                            onClick={handleCreateCampaign}
                            size='large'
                            type='submit'
                            sx={{ mr: 2 }}
                            variant='contained'>
                          Create
                        </Button>
                      </CardActions>
                    </Card>
                  </TabPanel>
              </> :

              <Box
                  justifyContent="center"
                  alignContent="center"
                  display="flex"
                  sx={{
                    mb: '2rem'
                  }}>
                <CircularProgress color="primary" />
              </Box>
          }
        </Grid>

      </Grid>
  )
}

export default EmailCampaigns;
