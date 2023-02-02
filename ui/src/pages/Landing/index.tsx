import React from 'react';
import Hero from "@/pages/Landing/Hero";
import Features from './Features';
import WhyKalico from "@/pages/Landing/WhyKalico";
import FAQ from './FAQ';
import CallToAction from './CallToAction';
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
          <Features/>
          <FAQ/>
          {/*<CallToAction/>*/}
        </main>
      </>
  );
}

export default Landing;
