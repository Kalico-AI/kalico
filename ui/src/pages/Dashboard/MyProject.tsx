import React, {FC} from 'react';
import {observer} from "mobx-react";
import {Grid} from "@mui/material";
import ForcedLayoutEditor from "@/components/SlateEditor/ForcedLayoutEditor";
import ExportDialog from "@/pages/Dashboard/ExportDialog";
import {ProjectDetail} from "@/api";
import {AuthUserContext} from "next-firebase-auth";
import {CenterAlignedProgress} from "@/utils/utils";

export interface MyProjectsProps {
  project: ProjectDetail,
  user: AuthUserContext,
  showProgress: boolean
}

const MyProject: FC<MyProjectsProps> = observer((props) => {
  return (
      <Grid container className="dashboard-container editor">
        <Grid item sm={12} sx={{width: '100%'}}>
          {
            props.showProgress ? <CenterAlignedProgress/> :
                <>
                  <ExportDialog/>
                  <ForcedLayoutEditor project={props.project} user={props.user}/>
                </>
          }
        </Grid>
      </Grid>
  );
})

export default MyProject;
