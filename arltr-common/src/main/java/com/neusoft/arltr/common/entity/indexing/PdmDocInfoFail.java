package com.neusoft.arltr.common.entity.indexing;


import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.util.Date;

/**
 * pdm文档同步失败记录表
 * @author yhh
 *
 */


@Entity
@Data
@Table(name ="PDM_DOC_INFO_FAIL")
public class PdmDocInfoFail {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE,generator="pdifseq")
    @SequenceGenerator(name="pdifseq",sequenceName="PDIF_SEQ",allocationSize=1)
    private  Integer id;
    /** 文档标识 */
    private  java.lang.String docId;
    /** 文档标题 */
    private  java.lang.String docTitle;
    /** pdm 数据表 id **/
    private Integer pdmDocInfoId;

    private String docUrl;

    private String docFilePath;

    private String failReason;

    private Integer failState;

    private Date cAt;

    private Date uAt;
    @Transient
    private Date startTime;
    @Transient
    private Date endTime;
    @Transient
    private String failStateTxt;

    public Integer getId() {
        return id;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getDocTitle() {
        return docTitle;
    }

    public void setDocTitle(String docTitle) {
        this.docTitle = docTitle;
    }

    public Integer getPdmDocInfoId() {
        return pdmDocInfoId;
    }

    public void setPdmDocInfoId(Integer pdmDocInfoId) {
        this.pdmDocInfoId = pdmDocInfoId;
    }

    public String getDocUrl() {
        return docUrl;
    }

    public void setDocUrl(String docUrl) {
        this.docUrl = docUrl;
    }

    public String getDocFilePath() {
        return docFilePath;
    }

    public void setDocFilePath(String docFilePath) {
        this.docFilePath = docFilePath;
    }

    public String getFailReason() {
        return failReason;
    }

    public void setFailReason(String failReason) {
        this.failReason = failReason;
    }

    public Integer getFailState() {
        return failState;
    }

    public void setFailState(Integer failState) {
        this.failState = failState;
    }

    public Date getcAt() {
        return cAt;
    }

    public void setcAt(Date cAt) {
        this.cAt = cAt;
    }

    public Date getuAt() {
        return uAt;
    }

    public void setuAt(Date uAt) {
        this.uAt = uAt;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getFailStateTxt() {
        return failState==null?"":3==failState?"文件路径无效":"索引建立失败";
    }

    public void setFailStateTxt(String failStateTxt) {
        this.failStateTxt = failStateTxt;
    }
}
