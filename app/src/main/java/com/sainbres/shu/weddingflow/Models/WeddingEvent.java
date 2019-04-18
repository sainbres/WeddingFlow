package com.sainbres.shu.weddingflow.Models;


import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.ForeignKeyReference;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.data.Blob;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.sainbres.shu.weddingflow.WeddingFlowDB;



@Table(database = WeddingFlowDB.class, name = "WeddingEvents")
public class WeddingEvent extends BaseModel {

    public WeddingEvent() {
    }

    @PrimaryKey(autoincrement = true)
    int EventId;

    @Column
    int UserId;

    @Column
    String Location;

    @Column
    String Participants;

    @Column
    String WeddingDate;

    @Column
    String Image;


    public int getEventId() {
        return EventId;
    }
    public void setEventId(int eventId) {
        EventId = eventId;
    }

    public int getUserId() { return this.UserId; }
    public void setUserId(int userId) { this.UserId = userId; }

    public String getLocation() { return Location; }
    public void setLocation(String location) { Location = location; }

    public String getParticipants() { return Participants; }
    public void setParticipants(String participants) { Participants = participants; }

    public String getWeddingDate() { return WeddingDate; }
    public void setWeddingDate(String weddingDate) { WeddingDate = weddingDate; }

    public String getImage() { return Image; }
    public void setImage(String image) { Image = image; }

}