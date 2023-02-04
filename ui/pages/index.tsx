import React from 'react';
import Landing from "@/pages/Landing";
import {CenterAlignedProgress} from "@/utils/utils";
import initAuth from "@/auth/nextAuth";
import {AuthAction, withAuthUser} from "next-firebase-auth";

initAuth()

function Index() {
  return (
    <>
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
