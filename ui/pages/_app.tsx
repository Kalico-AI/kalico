import type { AppProps } from "next/app";
import Head from 'next/head';
import {FC, ReactElement, ReactNode} from "react";
import {NextPage} from "next";
import { useState, useEffect } from "react";
import { useRouter } from "next/router";
// import "../styles/style.css";
// import "../styles/video.css";
// import "../styles/index.css";
// import "../styles/nav.css";
// import "../styles/tail.css";
// import "../styles/search.css";
import {RootStoreProvider} from "../store/RootStoreContext";
import Script from 'next/script'
import Footer from "@/components/Footer";
import HeaderNav from "@/components/Header";
// import Header from "@/layouts/v3/Header";

type NextPageWithLayout = NextPage & {
  getLayout?: (page: ReactElement) => ReactNode;
};

export async function getServerSideProps() {
  return {
    props: {
      title: "Kalico",
      description:"",
      siteImage: "https://"
    }
  }
}

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
  const meta = {
    title: "Kalico",
    description:"",
    siteImage: "https://"
  }

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
        <title>{meta.title}</title>
        <meta property="og:title" content={meta.title} key="title"/>
        <meta property="og:description" content={meta.description} key="description" />
        <meta property="og:image:secure" content={meta.siteImage} key="image:secure"/>






        {/*<link rel="stylesheet" href="/plugins/bootstrap/css/bootstrap.min.css"/>*/}

        {/*/!*Themify*!/*/}
        {/*<link rel="stylesheet" href="/plugins/themify/css/themify-icons.css"/>*/}
        {/*<link rel="stylesheet" href="/plugins/slick-carousel/slick-theme.css"/>*/}
        {/*<link rel="stylesheet" href="/plugins/slick-carousel/slick.css"/>*/}

        {/*/!*Slick Carousel*!/*/}
        {/*<link rel="stylesheet" href="/plugins/owl-carousel/owl.carousel.min.css"/>*/}
        {/*<link rel="stylesheet" href="/plugins/owl-carousel/owl.theme.default.min.css"/>*/}
        {/*<link rel="stylesheet" href="/plugins/magnific-popup/magnific-popup.css"/>*/}

        {/*manin stylesheet*/}
        {/*<link rel="stylesheet" href="/css/style.css"/>*/}
        {/*<link rel="stylesheet" href="/css/main_styles.css"/>*/}

        {/*<script src="/plugins/jquery/jquery.js"></script>*/}
        {/*<script src="/plugins/bootstrap/js/bootstrap.min.js"></script>*/}
        {/*<script src="/plugins/bootstrap/js/popper.min.js"></script>*/}
        {/*<script src="/plugins/owl-carousel/owl.carousel.min.js"></script>*/}
        {/*<script src="/plugins/slick-carousel/slick.min.js"></script>*/}
        {/*<script src="/plugins/magnific-popup/magnific-popup.js"></script>*/}
        {/*<script src="/js/swipe-deck.js"></script>*/}

      </Head>
        <RootStoreProvider>
          <HeaderNav/>
          {getLayout(<Component {...pageProps} />)}
          <Footer/>
        </RootStoreProvider>
    </>
  );
}

export default MyApp;
