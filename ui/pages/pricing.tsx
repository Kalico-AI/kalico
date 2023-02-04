import React from 'react';
import {CenterAlignedProgress} from "@/utils/utils";
import initAuth from "@/auth/nextAuth";
import {AuthAction, withAuthUser} from "next-firebase-auth";
import Pricing from "@/components/Pricing";

initAuth()

function Index() {
  return (
    <>
      <Pricing/>
    </>
  );
}
export default withAuthUser({
  whenAuthed: AuthAction.RENDER,
  whenUnauthedBeforeInit: AuthAction.SHOW_LOADER,
  whenUnauthedAfterInit: AuthAction.RENDER,
  LoaderComponent: () => <CenterAlignedProgress/>,
})(Index);
