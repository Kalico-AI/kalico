import React, {FC} from 'react';
import {observer} from "mobx-react";
import {useStore} from "@/hooks/useStore";
import {Box, Button} from "@mui/material";
import LogoutIcon from '@mui/icons-material/Logout';
import {auth} from "@/utils/firebase-setup";
import {useRouter} from "next/router";
import {PATHS} from "@/utils/constants";
import initAuth from "@/auth/nextAuth";
import {useAuthUser, withAuthUser} from "next-firebase-auth";
import ShoppingBasketIcon from '@mui/icons-material/ShoppingBasket';

initAuth()

const HeaderNav: FC<any> = observer((_props) => {
  const store = useStore()
  const router = useRouter()
  const user = useAuthUser()

  const logout = () => {
    auth.signOut().then(_ => {
      console.log("Logout success!")
      store.sessionDataStore.setUser(undefined)
      router.push({
        pathname: PATHS.LOGIN,
        query: { backTo: router.pathname }
      }).catch(e => console.log(e))
    }).catch(e => console.log(e))
  }

  let navbarClasses = "container-fluid"
  if (user && user.id) {
    navbarClasses = "container-fluid dashboard-navbar"
  }
  return (
      <header className="header-area">
        <nav className="navbar navbar-expand-lg menu_three sticky-nav">
          <div className={navbarClasses}>
            <a className="navbar-brand header_logo" href={user && user.id ? PATHS.DASHBOARD : PATHS.HOME}>
              <img className="main_logo" src="/assets/images/logo.png" alt="logo"/>
            </a>
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

              {user && user.id ? (
                  <>
                    <ul className="navbar-nav menu mx-auto">
                      {/*<li className="nav-item submenu mega-home active">*/}
                      {/*  <a href="/" className="nav-link dropdown-toggle active">Home</a>*/}
                      {/*</li>*/}
                      {/*<li className="nav-item dropdown submenu active">*/}
                      {/*  <a href="/#why-kalico" className="nav-link dropdown-toggle">Why Kalico</a>*/}
                      {/*</li>*/}
                      {/*<li className="nav-item dropdown submenu active">*/}
                      {/*  <a href="/#how-it-works" className="nav-link dropdown-toggle">How It Works</a>*/}
                      {/*</li>*/}

                      {/*<li className="nav-item dropdown submenu mega-menu active">*/}
                      {/*  <a href="/#features" className="nav-link dropdown-toggle">Features</a>*/}
                      {/*</li>*/}
                      {/*<li className="nav-item dropdown submenu mega-menu active">*/}
                      {/*  <a href="/#support" className="nav-link dropdown-toggle">Upgrade</a>*/}
                      {/*</li>*/}
                    </ul>
                    <div className="right-nav">
                      <Box sx={{mr: 3, mb: 1, mt: 1}}>
                      <Button
                          color="error"
                          startIcon={<ShoppingBasketIcon/>}
                          className="upgrade-button"
                          size='large'
                          variant='contained'
                          onClick={logout}
                      >Upgrade</Button>
                      </Box>
                      <Box sx={{ mb: 1, mt: 1}}>
                      <Button
                          color="warning"
                          startIcon={<LogoutIcon/>}
                          className="sign-in-button"
                          size='large'
                          variant='outlined'
                          onClick={logout}
                      />
                      </Box>
                    </div>
                  </>
              ) : (
                  <><ul className="navbar-nav menu mx-auto">
                    <li className="nav-item submenu mega-home active">
                      <a href="/" className="nav-link dropdown-toggle active">Home</a>
                    </li>
                    <li className="nav-item dropdown submenu active">
                      <a href="/#why-kalico" className="nav-link dropdown-toggle">Why Kalico</a>
                    </li>
                    <li className="nav-item dropdown submenu active">
                      <a href="/#how-it-works" className="nav-link dropdown-toggle">How It Works</a>
                    </li>

                    <li className="nav-item dropdown submenu mega-menu active">
                      <a href="/#features" className="nav-link dropdown-toggle">Features</a>
                    </li>
                    <li className="nav-item dropdown submenu mega-menu active">
                      <a href="/#support" className="nav-link dropdown-toggle">Support</a>
                    </li>
                  </ul>
                    <div className="right-nav">
                      {/*<a href="#" className="language-bar mr-50"><span className="active">En.</span><span>Ru</span></a>*/}
                      <a href={PATHS.LOGIN}>Sign in</a>
                      <a className="btn btn-red" href={PATHS.SIGN_UP}>Sign Up</a>
                    </div>
                  </>
                 )}
            </div>
          </div>
        </nav>
      </header>
  );
})

export default withAuthUser()(HeaderNav);
