import React, {FC, useEffect, useState} from 'react';
import {PATHS} from "@/utils/constants";
import {ProjectApi, ProjectDetail} from "@/api";
import {headerConfig} from "@/api/headerConfig";
import MyProject from "@/pages/Dashboard/MyProject";
import {toast} from "react-toastify";
import {useRouter} from "next/router";
import Head from "next/head";
import {auth} from "@/utils/firebase-setup";
import ListView from "@/pages/Recipe/ListView";



const items = [
  {
    slug: 'how-to-cook-chicken-well',
    date: 'March 1, 2023',
    title: 'How to Cook Frozen Pork Chops in the Oven',
    url: 'https://getonmyplate.com/how-to-cook-frozen-pork-chops-in-the-oven/',
    imgUrl: 'https://getonmyplate.com/wp-content/uploads/2023/02/frozen-pork-chops-in-the-oven-1-8-600x850.jpg',
    description: 'This Ground Beef Stir Fry Recipe is one you’ll add to your dinner rotation over and over! It uses simple ingredients and there are so many ways to customize it! Ground beef and veggies are coated in a delicious sweet…'
  },
  {
    slug: 'how-to-cook-chicken-well',
    description: 'This Ground Beef Stir Fry Recipe is one you’ll add to your dinner rotation over and over! It uses simple ingredients and there are so many ways to customize it! Ground beef and veggies are coated in a delicious sweet…',
    date: 'March 1, 2023',
    title: 'Velveeta Hamburger Helper {One Pot!}',
    url: 'https://getonmyplate.com/how-to-cook-frozen-pork-chops-in-the-oven/',
    imgUrl: 'https://getonmyplate.com/wp-content/uploads/2023/02/frozen-pork-chops-in-the-oven-1-8-600x850.jpg'
  },
  {
    slug: 'how-to-cook-chicken-well',
    description: 'This Ground Beef Stir Fry Recipe is one you’ll add to your dinner rotation over and over! It uses simple ingredients and there are so many ways to customize it! Ground beef and veggies are coated in a delicious sweet…',
    date: 'March 1, 2023',
    title: 'Homemade Spaghetti Sauce with Ground Beef',
    url: 'https://getonmyplate.com/how-to-cook-frozen-pork-chops-in-the-oven/',
    imgUrl: 'https://getonmyplate.com/wp-content/uploads/2023/02/frozen-pork-chops-in-the-oven-1-8-600x850.jpg'
  },
  {
    slug: 'how-to-cook-chicken-well',
    description: 'This Ground Beef Stir Fry Recipe is one you’ll add to your dinner rotation over and over! It uses simple ingredients and there are so many ways to customize it! Ground beef and veggies are coated in a delicious sweet…',
    date: 'March 1, 2023',
    title: 'How to Cook Frozen Pork Chops in the Oven',
    url: 'https://getonmyplate.com/how-to-cook-frozen-pork-chops-in-the-oven/',
    imgUrl: 'https://getonmyplate.com/wp-content/uploads/2023/02/frozen-pork-chops-in-the-oven-1-8-600x850.jpg'
  },
  {
    slug: 'how-to-cook-chicken-well',
    description: 'This Ground Beef Stir Fry Recipe is one you’ll add to your dinner rotation over and over! It uses simple ingredients and there are so many ways to customize it! Ground beef and veggies are coated in a delicious sweet…',
    date: 'March 1, 2023',
    title: 'How to Cook Frozen Pork Chops in the Oven',
    url: 'https://getonmyplate.com/how-to-cook-frozen-pork-chops-in-the-oven/',
    imgUrl: 'https://getonmyplate.com/wp-content/uploads/2023/02/frozen-pork-chops-in-the-oven-1-8-600x850.jpg'
  },
  {
    slug: 'how-to-cook-chicken-well',
    description: 'This Ground Beef Stir Fry Recipe is one you’ll add to your dinner rotation over and over! It uses simple ingredients and there are so many ways to customize it! Ground beef and veggies are coated in a delicious sweet…',
    date: 'March 1, 2023',
    title: 'How to Cook Frozen Pork Chops in the Oven',
    url: 'https://getonmyplate.com/how-to-cook-frozen-pork-chops-in-the-oven/',
    imgUrl: 'https://getonmyplate.com/wp-content/uploads/2023/02/frozen-pork-chops-in-the-oven-1-8-600x850.jpg'
  },
  {
    slug: 'how-to-cook-chicken-well',
    description: 'This Ground Beef Stir Fry Recipe is one you’ll add to your dinner rotation over and over! It uses simple ingredients and there are so many ways to customize it! Ground beef and veggies are coated in a delicious sweet…',
    date: 'March 1, 2023',
    title: 'How to Cook Frozen Pork Chops in the Oven',
    url: 'https://getonmyplate.com/how-to-cook-frozen-pork-chops-in-the-oven/',
    imgUrl: 'https://getonmyplate.com/wp-content/uploads/2023/02/frozen-pork-chops-in-the-oven-1-8-600x850.jpg'
  }
]

export async function getServerSideProps(context) {
  const page = context.query.page - 1
  const limit = 10
  return {
    props: {
      posts: items
    }
  }
}

interface RecipeListProps {
  posts: []
}


const RecipeList: FC<RecipeListProps> =  (props) => {
  const [project, setProject] = useState<ProjectDetail>()
  const [showProgress, setShowProgress] = useState(true)
  // const router = useRouter()

  // const fetchProjectById = () => {
  //   auth.onAuthStateChanged(user => {
  //     if (user) {
  //       user.getIdToken(false)
  //       .then(tokenResult => {
  //         const projectApi = new ProjectApi(headerConfig(tokenResult))
  //         projectApi.getProjectById(props.projectId)
  //         .then(response => {
  //           if (response.data && response.data) {
  //             setProject(response.data)
  //             setShowProgress(false)
  //           } else {
  //             toast("Sorry, we could not locate that project", {
  //               type: 'error',
  //               position: toast.POSITION.TOP_CENTER
  //             });
  //             router.push({
  //               pathname: PATHS.MY_PROJECTS
  //             }).then(_ => {
  //               setShowProgress(false)
  //             })
  //             .catch(e => console.log(e))
  //           }
  //         }).catch(e => console.log(e))
  //       }).catch(e => console.log(e))
  // }})}
  //
  // useEffect(() => {
  //   fetchProjectById()
  // }, [project])

  return (
      <>
        <Head>
          <title>Kalico | {project?.name}</title>
        </Head>
        <main>
          <section className="container">
            <ListView posts={props.posts}/>
          </section>
        </main>

      </>
  );
}

export default RecipeList
