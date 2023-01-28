import React, {FC} from 'react';
import {observer} from "mobx-react";
import { Grid} from "@mui/material";
import ForcedLayoutEditor from "@/components/SlateEditor/ForcedLayoutEditor";

export interface MyProjectsProps {

}

const Project: FC<MyProjectsProps> = observer((_props) => {
  return (
      <Grid container className="dashboard-container editor">
        <Grid item sm={12} sx={{width: '100%'}}>
          <ForcedLayoutEditor/>
        </Grid>
      </Grid>
  );
})

export default Project;
