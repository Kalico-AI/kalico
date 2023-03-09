import React from 'react';
import {CenterAlignedProgress} from "@/utils/utils";
import initAuth from "@/auth/nextAuth";
import {AuthAction, withAuthUser} from "next-firebase-auth";
import Head from "next/head";
import BetaProgram from "@/components/BetaProgram";

initAuth()

function Index() {
  return (
    <>
      <Head>
        <title>Kalico | Beta Program </title>
      </Head>
      <BetaProgram/>
    </>
  );
}
export default withAuthUser({
  whenAuthed: AuthAction.RENDER,
  whenUnauthedBeforeInit: AuthAction.SHOW_LOADER,
  whenUnauthedAfterInit: AuthAction.REDIRECT_TO_LOGIN,
  LoaderComponent: () => <CenterAlignedProgress/>,
})(Index);
