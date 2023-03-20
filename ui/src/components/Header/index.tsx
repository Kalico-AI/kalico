import React, {FC} from 'react';
import {observer} from "mobx-react";
import {Box} from "@mui/material";
import {PATHS} from "@/utils/constants";
import initAuth from "@/auth/nextAuth";
import {useAuthUser} from "next-firebase-auth";
import Link from "next/link";

initAuth()

export interface HeaderNavProps {
}

const HeaderNav: FC<HeaderNavProps> = observer((_props) => {
  const user = useAuthUser()

  let navbarClasses = "container-fluid"
  if (user && user.id) {
    navbarClasses = "container-fluid dashboard-navbar"
  }

  return (
      <header className="header">
        <nav className="navbar navbar-expand-lg menu_three sticky-nav">
          <div className={navbarClasses}>
            <button
                className="navbar-toggler collapsed"
                type="button"
                data-bs-toggle="collapse"
                data-bs-target="#navbarText"
                aria-controls="navbarText"
                aria-expanded="false"
                aria-label="Toggle navigation"
            >
            <span className="menu_toggle">
              <span className="hamburger">
                <span></span>
                <span></span>
                <span></span>
              </span>
              <span className="hamburger-cross">
                <span></span>
                <span></span>
              </span>
            </span>
            </button>
            <div className="collapse navbar-collapse justify-content-end" id="navbarText">
              <Box className="header-left">
                <Link className="navbar-brand header_logo" href={PATHS.HOME}>
                  <img className="main_logo" src="/assets/images/logo.png" alt="logo"/>
                </Link>
              </Box>
              <Box className="header-right">
                <a href="/"><p>Home</p></a>
                <a href="/recipes/1"><p>Recipes</p></a>
                <a href="/about"><p>About</p></a>
                {/*<a href={PATHS.LOGIN}><p>Sign In</p></a>*/}
              </Box>
            </div>
          </div>
        </nav>
      </header>
  );
})
export default HeaderNav;
