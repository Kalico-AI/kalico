import React from 'react';
import Head from "next/head";
import {AuthAction, withAuthUser} from "next-firebase-auth";
import FAQ from "@/pages/Landing/FAQ";

export async function getServerSideProps() {
    return {
      props: {
        title: "Kalico",
        description: "",
        siteImage: "https://"
      }
    }
}

const DashboardIndex =  (props) => {
    return (
        <>
          <Head>
            <title>{props.title}</title>
            <meta property="og:title" content={props.title} name="title" key="title"/>
            <meta property="og:description" content={props.description} name="description"
                  key="description"/>
            <meta property="og:image:secure" content={props.siteImage} name="image"
                  key="image:secure"/>
          </Head>
         <FAQ/>
        </>
    );
}

export default withAuthUser({
  whenAuthed: AuthAction.RENDER,
  whenUnauthedBeforeInit: AuthAction.RETURN_NULL,
  whenUnauthedAfterInit: AuthAction.REDIRECT_TO_LOGIN,
})(DashboardIndex);
