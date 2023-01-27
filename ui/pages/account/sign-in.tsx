import React from 'react';
import Head from "next/head";
import SignIn from "@/pages/SignIn";
import {SITE_IMAGE_URL} from "@/utils/constants";
import {AuthAction, withAuthUser} from "next-firebase-auth";

export async function getServerSideProps() {
  return {
    props: {
      title: "Kalico | Sign In",
      description: "Sign in to your Kalico account",
      siteImage: SITE_IMAGE_URL
    }
  }
}

function SignInIndex(props) {
  return (
    <>
      <Head>
        <title>{props.title}</title>
        <meta property="og:title" content={props.title} name="title" key="title"/>
        <meta property="og:description" content={props.description} name="description" key="description"/>
        <meta property="og:image:secure" content={props.siteImage} name="image" key="image:secure"/>
      </Head>
      <SignIn isSignUp={false}/>
    </>
  );
}


export default withAuthUser({
  whenAuthed: AuthAction.REDIRECT_TO_APP,
  whenUnauthedBeforeInit: AuthAction.RETURN_NULL,
  whenUnauthedAfterInit: AuthAction.RENDER,
})(SignInIndex);
