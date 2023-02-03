import React from 'react';
import Head from "next/head";
import {SITE_IMAGE_URL} from "@/utils/constants";
import {AuthAction, withAuthUser} from "next-firebase-auth";
import {CenterAlignedProgress} from "@/utils/utils";
import SignInConfirmation from "@/pages/SignIn/Confirmation";

export async function getServerSideProps() {
  return {
    props: {
      title: "Kalico | Email confirmation",
      description: "Confirm your email address",
      siteImage: SITE_IMAGE_URL
    }
  }
}

function SignInConfirmationIndex(props) {
  return (
    <>
      <Head>
        <title>{props.title}</title>
        <meta property="og:title" content={props.title} name="title" key="title"/>
        <meta property="og:description" content={props.description} name="description" key="description"/>
        <meta property="og:image:secure" content={props.siteImage} name="image" key="image:secure"/>
      </Head>
      <SignInConfirmation/>
    </>
  );
}


export default withAuthUser({
  whenAuthed: AuthAction.REDIRECT_TO_APP,
  whenUnauthedBeforeInit: AuthAction.SHOW_LOADER,
  whenUnauthedAfterInit: AuthAction.RENDER,
  LoaderComponent: () => <CenterAlignedProgress/>
})(SignInConfirmationIndex);
