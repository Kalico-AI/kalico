import React, {FC, useEffect, useState} from 'react';
import {AuthAction, useAuthUser, withAuthUser} from "next-firebase-auth";
import {EmailCampaign, LeadApi} from "@/api";
import {headerConfig} from "@/api/headerConfig";
import {CenterAlignedProgress} from "@/utils/utils";
import Head from "next/head";
import {ADMIN_EMAIL, PATHS} from "@/utils/constants";
import {useRouter} from "next/router";
import EmailCampaigns from "@/pages/Dashboard/EmailCampaigns";


export interface ProjectIndexProps {
  title?: string,
  description?: string,
  siteImage?: string
}

const CampaignsIndex: FC<ProjectIndexProps> =  (_props) => {
  const [emailCampaigns, setEmailCampaigns] = useState<EmailCampaign[]>([])
  const user = useAuthUser()
  const router = useRouter()

  const fetchCampaigns = () => {
    user.getIdToken(false)
    .then(tokenResult => {
      const leadApi = new LeadApi(headerConfig(tokenResult))
      leadApi.getEmailCampaignMetrics()
      .then(response => {
        if (response.data && response.data.campaigns) {
          setEmailCampaigns(response.data.campaigns)
        }
      }).catch(e => console.log(e))
    }).catch(e => console.log(e))
  }

  useEffect(() => {
    fetchCampaigns()
  }, [])

  if (user.email !== ADMIN_EMAIL) {
    router.push({
      pathname: PATHS.DASHBOARD
    }).catch(e => console.log(e))
  }

    return (
        <>
          <Head>
            <title>Kalico | Email Campaigns </title>
          </Head>
          <div style={{maxWidth: '650px', margin: '0 auto'}}>
            <EmailCampaigns campaigns={emailCampaigns} user={user}/>
          </div>

        </>
    );
}

export default withAuthUser({
  whenAuthed: AuthAction.RENDER,
  whenUnauthedBeforeInit: AuthAction.SHOW_LOADER,
  whenUnauthedAfterInit: AuthAction.REDIRECT_TO_LOGIN,
  LoaderComponent: () => <CenterAlignedProgress/>,
})(CampaignsIndex);
