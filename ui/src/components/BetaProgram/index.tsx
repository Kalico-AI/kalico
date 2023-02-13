import React from 'react';
import {withAuthUser} from "next-firebase-auth";
import {PATHS} from "@/utils/constants";
import dynamic from "next/dynamic";

const BetaProgram = () => {

  return (
      <section className="choose-plan-area-three bg-price pt-150 pb-20">
        <div className="container">
          <div className="section-title-center">
            <div className="beta-program-img">
              <img src="/assets/images/undraw_scientist.svg" alt="Scientist"
                   className="app-icon"/>
            </div>
            <h2 className="wow fadeInUp">Kalico Beta Software Program</h2>
          </div>
          <div className="col-12 wow fadeInUp" data-wow-delay="0.1s">
            <div className="tab-content features-tab-content pt-10 pb-50" id="nav-tabContent">
              <div className="tab-pane fade show active" id="nav-monthly" role="tabpanel"
                   aria-labelledby="nav-monthly-tab">
                <div className="row gy-xl-0 gy-4 pb-20">
                  <p className="wow fadeInUp" data-wow-delay="0.2s">
                    Kalico is currently in beta, and we need your help! Your feedback will help us catch bugs sooner and improve the features that you want to see the most. As a beta user, you will have a significant impact on the direction of the product. As a thank you, we will give you free Premium access to all our current and future features.
                  </p>
                </div>
                <div className="row gy-xl-0 gy-4 pb-200">
                  <p className="wow fadeInUp" data-wow-delay="0.2s">
                   If you would like to join our beta program, please send us an email to <strong>getkalicoai@gmail.com</strong> or send us a DM on <a href={PATHS.TWITTER_PAGE}>Twitter</a>.
                  </p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>
  );
}
export default dynamic(() => Promise.resolve(withAuthUser()(BetaProgram)), {
  ssr: false
})

