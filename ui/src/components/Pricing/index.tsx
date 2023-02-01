import React, {useEffect, useState} from 'react';
import {useAuthUser, withAuthUser} from "next-firebase-auth";
import {useRouter} from "next/router";
import {PATHS} from "@/utils/constants";
import dynamic from "next/dynamic";

const Pricing = () => {
  const user = useAuthUser()
  const router = useRouter()
  const [hide, setHide] = useState(true)

  useEffect(() => {
    const interval = setInterval(() => {
      setHide(false)
    }, 2000)
    if (user.id) {
      setHide(false)
    }
    return () => {
      clearInterval(interval)
    }
  }, [user.id])


  return (
      <section className="pricing-area section-padding-2">
        <div className="container">
          <div className="row">
            <div className="col-12">
              <div className="section-title-center">
                <h2 className="mt-n3">Choose a plan that works for you</h2>
              </div>
            </div>
          </div>
          <div className="tab-content" id="myTabContent">
            <div className="tab-pane fade show active" id="home" role="tabpanel"
                 aria-labelledby="home-tab">
              <div className="row">
                <div className="col-sm-6 col-xl-4">
                  <div className="pricing-item" style={{minHeight: '750px'}}>
                    <h3 className="pricing-title">Free</h3>
                    <div className="price"><span>Always Free</span></div>
                    <span className="pricing-item-user">Try out what Kalico has to offer</span>
                    <a href={PATHS.DASHBOARD} className="pricing-btn">Continue</a>
                    <ul style={{minHeight: '350px'}}>
                      <li className="text-black">1 user</li>
                      <li className="text-black">AI Content Repurposing</li>
                      <li className="text-black">Text Rewording</li>
                      <li className="text-black">Topic Discovery</li>
                      <li className="text-black">Human-like output</li>
                      <li className="text-black">5 Projects or 60 minutes of audio/video</li>
                    </ul>
                    <a href={PATHS.DASHBOARD} className="pricing-btn">Continue</a>
                  </div>
                </div>
                <div className="col-sm-6 col-xl-4">
                  <div className="pricing-item active" style={{minHeight: '750px'}}>
                    {/*<div className="badge">35%off</div>*/}
                    <h3 className="pricing-title">Premium</h3>
                    <div className="price">
                      {/*<span className="dollar">$59</span>*/}
                      {/*<span className="euro">&euro;7.09</span><sup> /user/mo</sup>*/}
                    </div>
                    <span className="pricing-item-user">For content creators and freelancers</span>
                    {/*<span className="pricing-item-user">Starts at $10.4 Includes 2 users</span>*/}
                    <a href="#" className="pricing-btn">Coming Soon</a>
                    <ul style={{minHeight: '350px'}}>
                      <li className="text-black">All of the features from the Free plan</li>
                      <li className="text-black">2 Additional Rewrites/project</li>
                      <li className="text-black">AI Image and GIF generation and embedding</li>
                      <li className="text-black">Publish to WordPress</li>
                      <li className="text-black">Premium Support</li>
                      <li className="text-black">Access to the latest features</li>
                      <li className="text-black">150 Projects or 1500 minutes of audio/video</li>
                    </ul>
                    <a href="#" className="pricing-btn">Coming Soon</a>
                  </div>
                </div>
                <div className="col-sm-6 col-xl-4">
                  <div className="pricing-item bg-yellow-200" style={{minHeight: '750px'}}>
                    {/*<div className="badge">50%off</div>*/}
                    <h3 className="pricing-title">Custom</h3>
                    <div className="price">
                      {/*<span className="dollar">$16</span>*/}
                      {/*<span className="euro">&euro;14.17</span><sup> /user/mo</sup>*/}
                    </div>
                    <span className="pricing-item-user">For enterprise users</span>
                    <a href="#" className="pricing-btn">Coming Soon</a>
                    <ul style={{minHeight: '370px'}}>
                      <li className="text-black">All Premium features</li>
                      <li className="text-black">Unlimited projects</li>
                      <li className="text-black">API Access</li>
                    </ul>
                    <a href="#" className="pricing-btn">Coming Soon</a>
                  </div>
                </div>
              </div>
              {/*<img src="/assets/images/most.png" alt="" className="popular d-none d-lg-block"/>*/}
            </div>
          </div>
        </div>
      </section>
  );
}
export default dynamic(() => Promise.resolve(withAuthUser()(Pricing)), {
  ssr: false
})

