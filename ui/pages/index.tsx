import React from 'react';
import Head from "next/head";
import RecipeLanding from "@/pages/RecipeLanding";


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
