import React, {FC, useEffect, useState} from 'react';
import {PATHS} from "@/utils/constants";
import {ProjectApi, ProjectDetail} from "@/api";
import {headerConfig} from "@/api/headerConfig";
import MyProject from "@/pages/Dashboard/MyProject";
import {toast} from "react-toastify";
import {useRouter} from "next/router";
import Head from "next/head";
import {auth} from "@/utils/firebase-setup";
import DetailView from "@/pages/Recipe/DetailView";

const post =  {
  summary: 'This Ground Beef Stir Fry Recipe is one you’ll add to your dinner rotation over and over! It uses simple ingredients and there are so many ways to customize it! Ground beef and veggies are coated in a delicious sweet and savory stir-fry sauce that everyone will love. The best part is….it’s done in about 20 minutes!',
  slug: 'how-to-cook-chicken-well',
  date: 'March 1, 2023',
  title: 'How to Cook Frozen Pork Chops in the Oven',
  url: 'https://getonmyplate.com/how-to-cook-frozen-pork-chops-in-the-oven/',
  imgUrl: 'https://picsum.photos/800/800',
  description: 'This Ground Beef Stir Fry Recipe is one you’ll add to your dinner rotation over and over! It uses simple ingredients and there are so many ways to customize it! Ground beef and veggies are coated in a delicious sweet…',
  ingredients: [
    "2 lbs boneless, skinless chicken chunks",
    "2 tablespoons freshly grated ginger root",
    "2 cloves garlic, finely minced",
    "2 tablespoons light brown sugar",
    "2 tablespoons rice vinegar",
    "2 tablespoons fish sauce",
    "1 tablespoon soy sauce",
    "Hot sauce, to taste",
    "1/4 cup chopped cilantro",
    "2 green onions, chopped",
    "1/4 cup roasted peanuts",
    "2 peppers (green jalapeno and red Jimmy dolo), sliced",
    "1 tablespoon vegetable oil"
  ],
  instructions: [
    "1. In a mixing bowl, combine grated ginger root, minced garlic, light brown sugar, rice vinegar, fish sauce, soy sauce and hot sauce. Whisk until combined.",
    "2. Place chicken chunks in a bowl and add 1/4 cup of the sauce mixture. Mix until all the chicken chunks are coated. Let sit for 15 minutes.",
    "3. Heat a heavy, large cast iron skillet over high heat. Add vegetable oil and let it smoke.",
    "4. Carefully add the chicken chunks to the skillet, spreading them out so they are in contact with the pan. Cook, stirring, until the chicken is caramelized.",
    "5. Reduce heat to medium and add the peppers, roasted peanuts and green onions. Cook for 1 minute.",
    "6. Add the remaining sauce and cook, stirring, until the sauce is thick and caramelized.",
    "7. Turn off the heat and stir in the chopped cilantro.",
    "8. Serve over rice and garnish with additional green onion, if desired. Enjoy!"
  ]
}
export async function getServerSideProps(context) {
  return {
    props: {
      post: post
    }
  }
}

interface RecipeDetailProps {
  post: object
}


const RecipeDetailIndex: FC<RecipeDetailProps> =  (props) => {
  const [project, setProject] = useState<ProjectDetail>()
  const [showProgress, setShowProgress] = useState(true)
  const router = useRouter()

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
          <DetailView post={props.post}/>
        </main>

      </>
  );
}

export default RecipeDetailIndex
