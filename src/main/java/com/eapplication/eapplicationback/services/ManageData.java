package com.eapplication.eapplicationback.services;

import com.eapplication.eapplicationback.constantes.Comments;
import com.eapplication.eapplicationback.models.nodes.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class ManageData {

    @Autowired
    ModelMapper modelMapper;

    public ManageData() {
    }

    /**
     * Construct List of {@link NodeType} from String code
     *
     * @param code
     * @return List<NodeType>
     */
    public List<NodeType> constructNodeTypeFromCode(String code) {

        List<NodeType> nodeTypeList = new ArrayList<>();
        String nodeTypeString = StringUtils.substringBetween(code, Comments.NODE_TYPE_START, Comments.COMMENT);

        if (nodeTypeString != null) {
            String[] lines = nodeTypeString.split("\\r?\\n");
            for (String line : lines) {

                String[] columns = line.split(Comments.COLUMNS_SEPARATOR);
                if (columns.length == 3) {
                    try {
                        nodeTypeList.add(NodeType.builder()
                                .id(Integer.parseInt(columns[1]))
                                .name(StringUtils.substringBetween(columns[2], "'", "'"))
                                .build());
                    } catch (NumberFormatException e) {
                        log.error("mince !");
                    }
                }

            }
        }

        return nodeTypeList;
    }


    /**
     * @param code
     * @return
     */
    public List<Entry> constructEntriesFromResponse(String code) {

        List<Entry> nodeTypeList = new ArrayList<>();
        String nodeTypeString = StringUtils.substringBetween(code, Comments.ENTRY_START, Comments.COMMENT);

        if (nodeTypeString != null) {
            String[] lines = nodeTypeString.split("\\r?\\n");
            for (String line : lines) {

                String[] columns = line.split(Comments.COLUMNS_SEPARATOR);
                if (columns.length >= 4) {
                    try {
                        nodeTypeList.add(Entry.builder()
                                .id(Integer.parseInt(columns[1]))
                                .name(StringUtils.substringBetween(columns[2], "'", "'"))
                                .nodeTypeId(Integer.parseInt(columns[3]))
                                .weight(Integer.parseInt(columns[4]))
                                .formatedName(columns.length > 5 ? StringUtils.substringBetween(columns[5], "'", "'") : "").build());
                    } catch (NumberFormatException e) {
                        log.error("ouh là !");
                    }
                }

            }
        }

        return nodeTypeList;
    }

    /**
     * @param code
     * @return
     */
    public List<RelationType> constructRelationTypeFromResponse(String code) {

        List<RelationType> relationTypeArrayList = new ArrayList<>();
        String relationTypeString = StringUtils.substringBetween(code, Comments.RELATION_TYPE_START, Comments.COMMENT);

        if (relationTypeString != null) {
            String[] lines = relationTypeString.split("\\r?\\n");
            final Pattern p = Pattern.compile("\\'([^\\\"]*)\\'");
            for (String line : lines) {

                String[] columns = line.split(Comments.COLUMNS_SEPARATOR);
                if (columns.length >= 4 && columns[1] != null) {
                    try {
                        Matcher m = p.matcher(columns[2]);
                        relationTypeArrayList.add(RelationType.builder()
                                .id(Integer.parseInt(columns[1]))
                                .name(m.find() ? m.group(1) : "")
                                .trgpName(columns.length == 4 ? "" : StringUtils.substringBetween(columns[3], "'", "'"))
                                .help(columns.length == 4 ? StringUtils.substringBetween(columns[3], "'", "'") : columns[4])
                                .build());
                    } catch (NumberFormatException e) {
                        log.error("what??");
                    }
                }
            }
        }
        return relationTypeArrayList;
    }

    /**
     * @param code
     * @return
     */
    public List<OutRelation> constructOutRelationFromCode(String code) {

        List<OutRelation> outrelationArrayList = new ArrayList<>();
        String nodeTypeString = StringUtils.substringBetween(code, Comments.OUT_RELATION_START, Comments.COMMENT);

        if (nodeTypeString != null) {
            String[] lines = nodeTypeString.split("\\r?\\n");
            for (String line : lines) {
                String[] columns = line.split(Comments.COLUMNS_SEPARATOR);
                if (columns.length >= 4) {
                    try {
                        outrelationArrayList.add(OutRelation.builder()
                                .id(Integer.parseInt(columns[1]))
                                .nodeId(Integer.parseInt(columns[2]))
                                .outNodeId(Integer.parseInt(columns[3]))
                                .relationTypeId(Integer.parseInt(columns[4]))
                                .weight(Integer.parseInt(columns[5]))
                                .build());
                    } catch (NumberFormatException e) {
                        log.error("ewww !");
                    }
                }
            }
        }
        return outrelationArrayList;
    }

    public List<InRelation> constructInRelationFromCode(String code) {

        List<InRelation> inrelationArrayList = new ArrayList<>();
        String nodeTypeString = StringUtils.substringBetween(code, Comments.IN_RELATION, Comments.COMMENT);

        if (nodeTypeString != null) {
            String[] lines = nodeTypeString.split("\\r?\\n");
            for (String line : lines) {
                String[] columns = line.split(Comments.COLUMNS_SEPARATOR);
                if (columns.length >= 4) {
                    try {
                        inrelationArrayList.add(InRelation.builder()
                                .id(Integer.parseInt(columns[1]))
                                .inNodeId(Integer.parseInt(columns[2]))
                                .nodeId(Integer.parseInt(columns[3]))
                                .relationTypeId(Integer.parseInt(columns[4]))
                                .weight(Integer.parseInt(columns[5]))
                                .build());
                    } catch (NumberFormatException e) {
                        log.error("non mais ...");
                    }
                }
            }
        }
        return inrelationArrayList;
    }

    public String getStringBetweenBalise(String start, String end, String text) {
        return StringUtils.substringBetween(text, start, end);
    }

    public String removeBaliseAndCarriageReturn(String text) {
//        return text.replaceAll("<[^>]*>", "");
        return text.replaceAll("<[^>]*>|\\n", "");
    }


}
