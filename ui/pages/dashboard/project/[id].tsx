import React, {FC, useEffect, useState} from 'react';
import {AuthAction, useAuthUser, withAuthUser} from "next-firebase-auth";
import {PATHS} from "@/utils/constants";
import {ProjectApi, ProjectDetail} from "@/api";
import {headerConfig} from "@/api/headerConfig";
import MyProject from "@/pages/Dashboard/MyProject";
import {toast} from "react-toastify";
import {useRouter} from "next/router";
import {CenterAlignedProgress} from "@/utils/utils";
import Head from "next/head";

export async function getServerSideProps(context) {
  return {
    props: {
      projectId: context.query.id
    }
  }
}

interface ProjectByIdProps {
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
          <title>Kalico | {project?.name}</title>
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
  whenUnauthedBeforeInit: AuthAction.SHOW_LOADER,
  whenUnauthedAfterInit: AuthAction.REDIRECT_TO_LOGIN,
  LoaderComponent: () => <CenterAlignedProgress/>,

})(ProjectByIdIndex);
