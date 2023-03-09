import React, {FC} from "react";
import moment from "moment";
import {DocumentDetail} from "@/types/types";

interface ListViewProps {
  posts?: DocumentDetail[],
  dashboard?: boolean
}

const ListView: FC<ListViewProps> = (props) => {
    return (
        <div className="container mx-auto px-2 py-4">
          {
            props?.posts?.map(item => {
              let url = '/post/' + item.slug
              if (props.dashboard) {
                url = '/editor/' + item.uid
              }
              let date = moment(item.createdAt * 1000).format('ll')
              return (
                <div className="py-2 mb-2 prose" key={item.slug}>
                  <a className="no-underline h5 bold text-accent"
                     href={url}>{date}</a>
                  <h2 className="h1 lh-condensed col-9 mt-0">
                    <a className="blog-link-primary"
                       href={url}>{item.title}</a>
                  </h2>
                  <p>{item.description}</p>
                </div>)
            })
          }
        </div>
    );
}

export default ListView;