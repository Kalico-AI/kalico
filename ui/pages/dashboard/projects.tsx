import React, {FC, useEffect, useState} from 'react';
import Head from "next/head";
import {AuthAction, useAuthUser, withAuthUser} from "next-firebase-auth";
import MyProjects from "@/pages/Dashboard/MyProjects";
import {Project, ProjectApi} from "@/api";
import {headerConfig} from "@/api/headerConfig";
import {SITE_IMAGE_URL} from "@/utils/constants";

export async function getServerSideProps() {
  return {
    props: {
      title: "Kalico",
      description: "",
      siteImage: SITE_IMAGE_URL
    }
  }
}

export interface ProjectIndexProps {
  title?: string,
  description?: string,
  siteImage?: string
}

const ProjectsIndex: FC<ProjectIndexProps> =  (props) => {
  const [projects, setProjects] = useState<Project[]>([])
  const user = useAuthUser()

  const fetchProjects = () => {
    user.getIdToken(false)
    .then(tokenResult => {
      const projectApi = new ProjectApi(headerConfig(tokenResult))
      projectApi.getAllProjects()
      .then(response => {
        if (response.data && response.data.records) {
          setProjects(response.data.records)
        }
      }).catch(e => console.log(e))
    }).catch(e => console.log(e))
  }

  useEffect(() => {
    fetchProjects()
  }, [!projects])

    return (
        <>
          <Head>
            <title>{props.title}</title>
            <meta property="og:title" content={props.title} name="title" key="title"/>
            <meta property="og:description" content={props.description} name="description"
                  key="description"/>
            <meta property="og:image:secure" content={props.siteImage} name="image"
                  key="image:secure"/>
          </Head>
          <main>
            <section className="container">
              <MyProjects projects={projects}/>
            </section>
          </main>
        </>
    );
}

export default withAuthUser({
  whenAuthed: AuthAction.RENDER,
  whenUnauthedBeforeInit: AuthAction.SHOW_LOADER,
  whenUnauthedAfterInit: AuthAction.REDIRECT_TO_LOGIN
})(ProjectsIndex);
