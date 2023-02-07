import React from 'react';
import SignIn from "@/pages/SignIn";
import {AuthAction, withAuthUser} from "next-firebase-auth";
import {CenterAlignedProgress} from "@/utils/utils";
import Head from "next/head";

export async function getServerSideProps(context) {
  return {
    props: {
      confirmationFailed: context.query.confirmationFailed ? context.query.confirmationFailed : false
    }
  }
}

function SignInIndex(props) {
  return (
    <>
      <Head>
        <title>Kalico | Sign In</title>
      </Head>
      <SignIn confirmationFailed={props.confirmationFailed}/>
    </>
  );
}


export default withAuthUser({
  whenAuthed: AuthAction.REDIRECT_TO_APP,
  whenUnauthedBeforeInit: AuthAction.SHOW_LOADER,
  whenUnauthedAfterInit: AuthAction.RENDER,
  LoaderComponent: () => <CenterAlignedProgress/>,
})(SignInIndex);
