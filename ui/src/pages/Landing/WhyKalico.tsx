import React from 'react';
import LanguageIcon from '@mui/icons-material/Language';
import PaidIcon from '@mui/icons-material/Paid';
import TrendingUpIcon from '@mui/icons-material/TrendingUp';
import CreditCardIcon from '@mui/icons-material/CreditCard';
import {Box} from "@mui/material";


function WhyKalico() {
  return (
      <section className="hardware-support-area pt-150 pb-40" id="why-kalico">
        <div className="container">
          <div className="row align-items-center">
            <div className="col-lg-8">
              <div className="section-title-left pb-30">
                <h2>Why use Kalico?</h2>
              </div>
              <div className="row">
                <div className="col-md-6">
                  <ul className="hardware-list">
                    <li>
                      <Box sx={{display: 'inline-flex'}}>
                        <PaidIcon color="warning"/>
                        <h4 style={{marginLeft: '10px'}}>Save time and money</h4>
                      </Box>
                      <Box sx={{ml: 4, pb: 1, fontSize: '14px'}}>
                        <p>On average, Fiverr freelancers charges $125 for a 6-hour turnaround time per article. In contrast, Kalico can generate superior quality content in mere minutes for less than 1% of the cost!</p>
                      </Box>
                    </li>
                    <li>
                      <Box sx={{display: 'inline-flex'}}>
                        <TrendingUpIcon color="warning"/>
                        <h4 style={{marginLeft: '10px'}}>10X your content's reach</h4>
                      </Box>
                      <Box sx={{ml: 4, pb: 1, fontSize: '14px'}}>
                        <p>Reach more audience and potential customers through SEO by with Kalico-generated content. We also support accurate translation across numerous languages, including English, Spanish, Hindi, and many more, for a strong global presence (coming soon). </p>
                      </Box>
                    </li>
                  </ul>
                </div>
                <div className="col-md-6">
                  <ul className="hardware-list">
                    <li>
                      <Box sx={{display: 'inline-flex'}}>
                        <CreditCardIcon color="warning"/>
                        <h4 style={{marginLeft: '10px'}}>Monetize your content</h4>
                      </Box>
                      <Box sx={{ml: 4, pb: 1, fontSize: '14px'}}>
                        <p>Syndicate the generated articles throughout your niche or turn them into eBooks.</p>
                      </Box>
                    </li>
                    <li>
                      <Box sx={{display: 'inline-flex'}}>
                        <LanguageIcon color="warning"/>
                        <h4 style={{marginLeft: '10px'}}>Make the Internet a better place</h4>
                      </Box>
                      <Box sx={{ml: 4, pb: 1, fontSize: '14px'}}>
                        <p>Popular AI writing tools often invent content, reducing the artistic and cultural value of the Internet. In stark contrast, Kalico's content generation retains the contextual integrity of your work and makes it accessible to a wider audience, amplifying our unique cultural identity.</p>
                      </Box>
                    </li>
                  </ul>
                </div>
              </div>
            </div>
            <div className="col-lg-4">
              <div className="available">
                <img src="/assets/images/landing/undraw_super_woman.svg" alt="Icon"
                     className="app-icon"/>
              </div>
            </div>
          </div>
        </div>
      </section>
  );
}

export default WhyKalico;
