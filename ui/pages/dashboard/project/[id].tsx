import React, {FC, useEffect, useState} from 'react';
import Head from "next/head";
import {AuthAction, useAuthUser, withAuthUser} from "next-firebase-auth";
import {PATHS, SITE_IMAGE_URL} from "@/utils/constants";
import {ProjectApi, ProjectDetail} from "@/api";
import {headerConfig} from "@/api/headerConfig";
import MyProject from "@/pages/Dashboard/MyProject";
import {toast} from "react-toastify";
import {useRouter} from "next/router";

export async function getServerSideProps(context) {
  return {
    props: {
      title: "Kalico",
      description: "",
      siteImage: SITE_IMAGE_URL,
      projectId: context.query.id
    }
  }
}

interface ProjectByIdProps {
  title?: string,
  description?: string,
  siteImage?: string,
  projectId: number
}


const ProjectByIdIndex: FC<ProjectByIdProps> =  (props) => {
  const [project, setProject] = useState<ProjectDetail>()
  const [showProgress, setShowProgress] = useState(true)
  const user = useAuthUser()
  const router = useRouter()

  const fetchProjectById = () => {
    user.getIdToken(false)
    .then(tokenResult => {
      const projectApi = new ProjectApi(headerConfig(tokenResult))
      projectApi.getProjectById(props.projectId)
      .then(response => {
        if (response.data && response.data) {
          setProject(response.data)
          setShowProgress(false)
        } else {
          toast("Sorry, we could not locate that project", {
            type: 'error',
            position: toast.POSITION.TOP_CENTER
          });
          router.push({
            pathname: PATHS.MY_PROJECTS
          }).then(_ => {
            setShowProgress(false)
          })
          .catch(e => console.log(e))
        }
      }).catch(e => console.log(e))
    }).catch(e => console.log(e))
  }

  useEffect(() => {
    fetchProjectById()
  }, [project])

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
            <MyProject project={project} user={user} showProgress={showProgress}/>
          </section>
        </main>

      </>
  );
}

export default withAuthUser({
  whenAuthed: AuthAction.RENDER,
  whenUnauthedBeforeInit: AuthAction.RETURN_NULL,
  whenUnauthedAfterInit: AuthAction.REDIRECT_TO_LOGIN
})(ProjectByIdIndex);
