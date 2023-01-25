import React from 'react';
import Hero from "@/pages/Landing/Hero";
import Clients from './Clients';
import Features from './Features';
import WhyKalico from "@/pages/Landing/WhyKalico";
import FeaturesTwo from "@/pages/Landing/FeaturesTwo";
import FeaturesThree from './FeaturesThree';
import FeaturesFour from "@/pages/Landing/FeaturesFour";
import FAQ from './FAQ';
import CallToAction from './CallToAction';
import FeaturesFive from "@/pages/Landing/FeaturesFive";
import Integration from "@/pages/Landing/Integration";
import HowItWorks from "@/pages/Landing/HowItWorks";
import VideoDemo from "@/pages/Landing/VideoDemo";

function Landing() {
  return (
      <>
        <main>
          <Hero/>
          <VideoDemo/>
          <WhyKalico/>
          <HowItWorks/>

          {/*<Features/>*/}

          {/*<FeaturesTwo/>*/}
          {/*<FeaturesThree/>*/}
          {/*<FeaturesFour/>*/}
          <FeaturesFive/>
          {/*<Integration/>*/}
          <FAQ/>
          <CallToAction/>
        </main>
      </>
  );
}

export default Landing;
