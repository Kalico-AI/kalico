import React from 'react';
import Head from "next/head";
import SignUp from "@/pages/SignUp";
// import SearchBox from "@/components/v1/SearchPage";
export async function getServerSideProps() {
  return {
    props: {
      title: "Kalico",
      description: "",
      siteImage: "https://"
    }
  }
}

function Index(props) {
  // Check that the DOM has loaded before rendering the page so that
  // we don't get a page without the CSS
  return (
    <>
      <Head>
        <title>{props.title}</title>
        <meta property="og:title" content={props.title} name="title" key="title"/>
        <meta property="og:description" content={props.description} name="description" key="description"/>
        <meta property="og:image:secure" content={props.siteImage} name="image" key="image:secure"/>
      </Head>
      <SignUp/>
    </>
  );
}

export default Index;
