import type { AppProps } from "next/app";
import Head from 'next/head';
import React, {FC, ReactElement, ReactNode} from "react";
import {NextPage} from "next";
import { useState, useEffect } from "react";
import { useRouter } from "next/router";

import {RootStoreProvider} from "@/store/RootStoreContext";
import Script from 'next/script'
import Footer from "@/components/Footer";
import HeaderNav from "@/components/Header";
import initAuth from "@/auth/nextAuth";
import {withAuthUser} from "next-firebase-auth";
import {Box, CircularProgress} from "@mui/material";
import 'react-toastify/dist/ReactToastify.css';

type NextPageWithLayout = NextPage & {
  getLayout?: (page: ReactElement) => ReactNode;
};


// Init Next Firebase Auth
initAuth()

interface DefaultAppProps extends AppProps {
  Component: NextPageWithLayout;
  title: string;
  description: string;
  siteImage: string;
}

const MyApp: FC<DefaultAppProps> = (props) => {
  const { Component, pageProps } = props;
  const getLayout = Component.getLayout ?? ((page) => page);
  const [_isProgress, setIsProgress] = useState<boolean>(false);
  const router = useRouter();

  useEffect(() => {
    const start = () => {
      setIsProgress(true);
    };
    const stop = () => {
      setIsProgress(false);
    };

    router.events.on("routeChangeStart", start);
    router.events.on("routeChangeComplete", stop);
    router.events.on("routeChangeError", stop);

    return () => {
      router.events.off("routeChangeStart", start);
      router.events.off("routeChangeComplete", stop);
      router.events.off("routeChangeError", stop);
    };
  }, [router]);

  return (
    <>
      <Script
          strategy="lazyOnload"
          src={`https://www.googletagmanager.com/gtag/js?id=${process.env.REACT_APP_GOOGLE_ANALYTICS}`}
      />
      <Script id="google-analytics" strategy="lazyOnload">
        {`
            window.dataLayer = window.dataLayer || [];
            function gtag(){dataLayer.push(arguments);}
            gtag('js', new Date());
            gtag('config', '${process.env.REACT_APP_GOOGLE_ANALYTICS}', {
              page_path: window.location.pathname,
            });
                `}
      </Script>
      <Head>
        <title>{props.title}</title>
        <meta property="og:title" content={props.title} key="title"/>
        <meta property="og:description" content={props.description} key="description" />
        <meta property="og:image:secure" content={props.siteImage} key="image:secure"/>
      </Head>
        <RootStoreProvider>
          <HeaderNav/>
          {getLayout(<Component {...pageProps} />)}
          <Footer/>
        </RootStoreProvider>
    </>
  );
}
const Loader = () => {
  return (
      <Box sx={{ display: 'flex' }}>
        <CircularProgress />
      </Box>
  );
}

export default withAuthUser({
  LoaderComponent: () => <Loader/>,
})(MyApp);
