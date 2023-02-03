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
      <section className="choose-plan-area-three bg-price pt-150 pb-150">
        <div className="container">
          <div className="section-title-center">
            <h2 className="wow fadeInUp">Choose a plan that works for you</h2>
            <p className="wow fadeInUp" data-wow-delay="0.2s">
             Try Kalico for free. No credit card required. Or upgrade to our paid plans (coming soon).
            </p>
          </div>
          <div className="col-12 wow fadeInUp" data-wow-delay="0.1s">
            <div className="tab-content features-tab-content pt-100 pb-100" id="nav-tabContent">
              <div className="tab-pane fade show active" id="nav-monthly" role="tabpanel"
                   aria-labelledby="nav-monthly-tab">
                <div className="row gy-xl-0 gy-4 pricing-item-two-cotnainer">
                  <div className="col-xl-4 col-md-6">
                    <div className="pricing-item wow fadeInUp" data-wow-delay="0.1s">
                      <h4>Free</h4>
                      <div className="price">$0<span>/mo</span></div>
                      <div className="list-wrapp">
                        <p className="list-title">Includes:</p>
                        <ul>
                          <li><i className="icon-check fas fa-check-circle"></i>1 User</li>
                          <li><i className="icon-check fas fa-check-circle"></i>3 Projects or 30 minutes</li>
                          <li><i className="icon-check fas fa-check-circle"></i>AI content repurposing</li>
                          <li><i className="icon-check fas fa-check-circle"></i>Text rewording</li>
                          <li><i className="icon-check fas fa-check-circle"></i>Topic discovery</li>
                          <li><i className="icon-close fas fa-times-circle"></i>Additional rewrites</li>
                          <li><i className="icon-close fas fa-times-circle"></i>Intelligent image and GIF embedding</li>
                          <li><i className="icon-close fas fa-times-circle"></i>Image and GIF hosting</li>
                          <li><i className="icon-close fas fa-times-circle"></i>Summarize</li>
                          <li><i className="icon-close fas fa-times-circle"></i>Publish to WordPress</li>
                          <li><i className="icon-close fas fa-times-circle"></i>Premium support</li>
                          <li><i className="icon-close fas fa-times-circle"></i>Access to latest features</li>
                          <li><i className="icon-close fas fa-times-circle"></i>5X faster processing</li>
                          <li><i className="icon-close fas fa-times-circle"></i>API access</li>
                          <li><i className="icon-close fas fa-times-circle"></i>Integrations</li>
                        </ul>
                      </div>
                      <a href={PATHS.DASHBOARD}  className="pricing-btn">Continue</a>
                    </div>
                  </div>
                  <div className="col-xl-4 col-md-6">
                    <div className="pricing-item active wow fadeInUp" data-wow-delay="0.3s">
                      <h4>Premium</h4>
                      <div className="price">$99<span>/mo</span></div>
                      <div className="list-wrapp">
                        <p className="list-title">Includes:</p>
                        <ul>
                          <li><i className="icon-check fas fa-check-circle"></i>1 User</li>
                          <li><i className="icon-check fas fa-check-circle"></i>100 Projects or 1,000 minutes</li>
                          <li><i className="icon-check fas fa-check-circle"></i>AI content repurposing</li>
                          <li><i className="icon-check fas fa-check-circle"></i>Text rewording</li>
                          <li><i className="icon-check fas fa-check-circle"></i>Topic discovery</li>
                          <li><i className="icon-check fas fa-check-circle"></i>Additional rewrites</li>
                          <li><i className="icon-check fas fa-check-circle"></i>Intelligent image and GIF embedding</li>
                          <li><i className="icon-check fas fa-check-circle"></i>Image and GIF hosting</li>
                          <li><i className="icon-check fas fa-check-circle"></i>Summarize</li>
                          <li><i className="icon-check fas fa-check-circle"></i>Publish to WordPress</li>
                          <li><i className="icon-check fas fa-check-circle"></i>Premium support</li>
                          <li><i className="icon-check fas fa-check-circle"></i>Access to latest features</li>
                          <li><i className="icon-check fas fa-check-circle"></i>5X faster processing</li>
                          <li><i className="icon-close fas fa-times-circle"></i>API access</li>
                          <li><i className="icon-close fas fa-times-circle"></i>Integrations</li>
                        </ul>
                      </div>
                      <a href="#"  className="pricing-btn">Coming Soon</a>
                    </div>
                  </div>
                  <div className="col-xl-4 col-md-6 mx-auto">
                    <div className="pricing-item wow fadeInUp" data-wow-delay="0.5s">
                      <h4>Custom</h4>
                      <div className="price">Contact Us<span></span></div>
                      <div className="list-wrapp">
                        <p className="list-title">Includes:</p>
                        <ul>
                          <li><i className="icon-check fas fa-check-circle"></i>1 User</li>
                          <li><i className="icon-check fas fa-check-circle"></i>Unlimited Projects and unlimited minutes</li>
                          <li><i className="icon-check fas fa-check-circle"></i>AI content repurposing</li>
                          <li><i className="icon-check fas fa-check-circle"></i>Text rewording</li>
                          <li><i className="icon-check fas fa-check-circle"></i>Topic discovery</li>
                          <li><i className="icon-check fas fa-check-circle"></i>Additional rewrites</li>
                          <li><i className="icon-check fas fa-check-circle"></i>Intelligent image and GIF embedding</li>
                          <li><i className="icon-check fas fa-check-circle"></i>Image and GIF hosting</li>
                          <li><i className="icon-check fas fa-check-circle"></i>Summarize</li>
                          <li><i className="icon-check fas fa-check-circle"></i>Publish to WordPress</li>
                          <li><i className="icon-check fas fa-check-circle"></i>Premium support</li>
                          <li><i className="icon-check fas fa-check-circle"></i>Access to latest features</li>
                          <li><i className="icon-check fas fa-check-circle"></i>5X faster processing</li>
                          <li><i className="icon-check fas fa-check-circle"></i>API access</li>
                          <li><i className="icon-check fas fa-check-circle"></i>Integrations</li>
                        </ul>
                      </div>
                      <a href="#" className="pricing-btn">Coming Soon</a>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>
  );
}
export default dynamic(() => Promise.resolve(withAuthUser()(Pricing)), {
  ssr: false
})

