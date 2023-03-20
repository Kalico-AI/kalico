import React from 'react';
import initAuth from "@/auth/nextAuth";
import Head from "next/head";
import RecipeLanding from "@/pages/RecipeLanding";

initAuth()

function Index() {
  return (
    <>
      <Head>
        <title>Kalico | Best Recipes</title>
      </Head>
      <RecipeLanding/>
    </>
  );
}
export default Index
// export default withAuthUser({
//   whenAuthed: AuthAction.REDIRECT_TO_APP,
//   whenUnauthedBeforeInit: AuthAction.SHOW_LOADER,
//   whenUnauthedAfterInit: AuthAction.RENDER,
//   LoaderComponent: () => <CenterAlignedProgress/>,
// })(Index);
