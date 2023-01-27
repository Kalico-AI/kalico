import React from 'react';
import Head from "next/head";
import SignIn from "@/pages/SignIn";
import {SITE_IMAGE_URL} from "@/utils/constants";

export async function getServerSideProps() {
  return {
    props: {
      title: "Kalico | Sign Up",
      description: "Create a new account on Kalico",
      siteImage: SITE_IMAGE_URL
    }
  }
}

function Index(props) {
  return (
    <>
      <Head>
        <title>{props.title}</title>
        <meta property="og:title" content={props.title} name="title" key="title"/>
        <meta property="og:description" content={props.description} name="description" key="description"/>
        <meta property="og:image:secure" content={props.siteImage} name="image" key="image:secure"/>
      </Head>
      <SignIn isSignUp={true}/>
    </>
  );
}

export default Index;
