import React from 'react';
import Head from "next/head";
import Privacy from "@/pages/Legal/Privacy";
export async function getServerSideProps() {
  return {
    props: {
      title: "Kalico",
      description: "",
      siteImage: "https://"
    }
  }
}

function LegalPrivacyIndex(props) {
  return (
      <>
        <Head>
          <title>{props.title}</title>
          <meta property="og:title" content={props.title} name="title" key="title"/>
          <meta property="og:description" content={props.description} name="description" key="description"/>
          <meta property="og:image:secure" content={props.siteImage} name="image" key="image:secure"/>
        </Head>
        <Privacy title="Privacy Policy" body=""/>
      </>
  );
}

export default LegalPrivacyIndex;
