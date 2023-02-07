import React, {FC, useEffect, useState} from 'react';
import {AuthAction, useAuthUser, withAuthUser} from "next-firebase-auth";
import MyProjects from "@/pages/Dashboard/MyProjects";
import {Project, ProjectApi} from "@/api";
import {headerConfig} from "@/api/headerConfig";
import {CenterAlignedProgress} from "@/utils/utils";
import Head from "next/head";


export interface ProjectIndexProps {
  title?: string,
  description?: string,
  siteImage?: string
}

const ProjectsIndex: FC<ProjectIndexProps> =  (_props) => {
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
            <title>Kalico | My Projects</title>
          </Head>
          <main>
            <section className="container">
              <MyProjects projects={projects} user={user}/>
            </section>
          </main>
        </>
    );
}

export default withAuthUser({
  whenAuthed: AuthAction.RENDER,
  whenUnauthedBeforeInit: AuthAction.SHOW_LOADER,
  whenUnauthedAfterInit: AuthAction.REDIRECT_TO_LOGIN,
  LoaderComponent: () => <CenterAlignedProgress/>,
})(ProjectsIndex);
