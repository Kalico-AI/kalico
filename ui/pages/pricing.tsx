import React from 'react';
import Head from "next/head";
import {CenterAlignedProgress} from "@/utils/utils";
import {SITE_IMAGE_URL} from "@/utils/constants";
import initAuth from "@/auth/nextAuth";
import {AuthAction, withAuthUser} from "next-firebase-auth";
import Pricing from "@/components/Pricing";

export async function getServerSideProps() {
  return {
    props: {
      title: "Kalico",
      description: "",
      siteImage: SITE_IMAGE_URL
    }
  }
}
initAuth()

function Index(props) {
  return (
    <>
      <Head>
        <title>{props.title}</title>
        <meta property="og:title" content={props.title} name="title" key="title"/>
        <meta property="og:description" content={props.description} name="description" key="description"/>
        <meta property="og:image:secure" content={props.siteImage} name="image" key="image:secure"/>
      </Head>
      <Pricing/>
    </>
  );
}
export default withAuthUser({
  LoaderComponent: () => <CenterAlignedProgress/>,
})(Index);
