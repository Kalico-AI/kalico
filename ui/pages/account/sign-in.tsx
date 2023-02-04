import React from 'react';
import SignIn from "@/pages/SignIn";
import {AuthAction, withAuthUser} from "next-firebase-auth";
import {CenterAlignedProgress} from "@/utils/utils";

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
