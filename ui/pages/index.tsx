import React from 'react';
import Landing from "@/pages/Landing";
import {CenterAlignedProgress} from "@/utils/utils";
import initAuth from "@/auth/nextAuth";
import {AuthAction, withAuthUser} from "next-firebase-auth";
import Head from "next/head";

initAuth()

function Index() {
  return (
    <>
      <Head>
        <title>Kalico | Audio and Video Content Repurposing</title>
      </Head>
      <Landing/>
    </>
  );
}
export default withAuthUser({
  whenAuthed: AuthAction.REDIRECT_TO_APP,
  whenUnauthedBeforeInit: AuthAction.SHOW_LOADER,
  whenUnauthedAfterInit: AuthAction.RENDER,
  LoaderComponent: () => <CenterAlignedProgress/>,
})(Index);
