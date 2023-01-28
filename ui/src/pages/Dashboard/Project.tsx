import React, {FC} from 'react';
import {observer} from "mobx-react";
import {Grid} from "@mui/material";
import ForcedLayoutEditor from "@/components/SlateEditor/ForcedLayoutEditor";
import ExportDialog from "@/pages/Dashboard/ExportDialog";

export interface MyProjectsProps {

}

const Project: FC<MyProjectsProps> = observer((_props) => {
  return (
      <Grid container className="dashboard-container editor">
        <Grid item sm={12} sx={{width: '100%'}}>
          <ExportDialog/>
          <ForcedLayoutEditor/>
        </Grid>
      </Grid>
  );
})

export default Project;
