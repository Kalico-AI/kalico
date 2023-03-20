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
import {AuthAction, withAuthUser, withAuthUserTokenSSR} from "next-firebase-auth";
import 'react-toastify/dist/ReactToastify.css';
import {CenterAlignedProgress} from "@/utils/utils";
import "style/blog.css"
import "style/food-blog.css"

type NextPageWithLayout = NextPage & {
  getLayout?: (page: ReactElement) => ReactNode;
};

// Init Next Firebase Auth
initAuth()

export const getServerSideProps = withAuthUserTokenSSR({
})(async ({ AuthUser }) => {
  return {
    props: {
      title: "Kalico AI | Video and Audio Content Re-purposing",
      userId: AuthUser.id
    }
  }
})

interface DefaultAppProps extends AppProps {
  Component: NextPageWithLayout;
  title: string;
  userId?: string,
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
      {process.env.NODE_ENV === 'production' &&
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
          </>
      }
      <Head>
        <title>{props.title}</title>
      </Head>
        <RootStoreProvider>
          <HeaderNav/>
          <div className="global-container">
          {getLayout(<Component {...pageProps} />)}
          </div>
          <Footer/>
        </RootStoreProvider>
      <style jsx global>
        {`

            .global-container {
              min-height: 90vh;
            }

          `}
      </style>
    </>
  );
}

export default withAuthUser({
  whenAuthed: AuthAction.RENDER,
  whenUnauthedBeforeInit: AuthAction.SHOW_LOADER,
  whenUnauthedAfterInit: AuthAction.RENDER,
  LoaderComponent: () => <CenterAlignedProgress/>
})(MyApp);


