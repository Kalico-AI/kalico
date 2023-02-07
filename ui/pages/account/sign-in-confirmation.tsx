import React from 'react';
import {AuthAction, withAuthUser} from "next-firebase-auth";
import {CenterAlignedProgress} from "@/utils/utils";
import SignInConfirmation from "@/pages/SignIn/Confirmation";
import Head from "next/head";



function SignInConfirmationIndex() {
  return (
    <>
      <Head>
        <title>Kalico | Sign In Confirmation</title>
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
