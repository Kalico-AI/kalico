import React from 'react';
import {useAuthUser} from "next-firebase-auth";
import {useRouter} from "next/router";
import {PATHS} from "@/utils/constants";

const Footer = () => {
  const user = useAuthUser()
  const router = useRouter()

  if (user && user.id && router.pathname.includes(PATHS.DASHBOARD)) {
    return <></>
  }

  return (
      <footer className="footer-cloud">
        <div className="footer-copyright pb-10 pt-20">
          <div className="container">
            <div className="row align-items-center">
              <div className="col-md-3">
                <a href="/"><img className="d-md-block d-sm-inline-block"
                     src="/assets/images/logo.png" alt="Kalico Logo" width={128}/></a>
              </div>
              <div className="col-lg-6 col-md-5">
                <p className="copyright-text text-md-center">Copyright 2023, All Rights Reserved</p>
              </div>
              <div className="col-lg-3 col-md-4">
                <a href={PATHS.TWITTER_PAGE} style={{textDecoration: 'none', color: 'grey'}}><i className="fab fa-twitter"></i></a>
              </div>
            </div>
          </div>
        </div>
      </footer>
  );
}
export default Footer

