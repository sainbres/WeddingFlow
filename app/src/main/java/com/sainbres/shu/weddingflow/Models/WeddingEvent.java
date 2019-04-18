
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

    @ForeignKey(stubbedRelationship = true, saveForeignKeyModel = false, references = {@ForeignKeyReference(columnName = "User", foreignKeyColumnName = "UserId")})
    User user;

    //int UserId;

    @Column
    String Location;

    @Column
    String Participants;

    @Column
    String WeddingDate;

    @Column
    Blob Image;


    public int getEventId() {
        return EventId;
    }
    public void setEventId(int eventId) {
        EventId = eventId;
    }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getLocation() { return Location; }
    public void setLocation(String location) { Location = location; }

    public String getParticipants() { return Participants; }
    public void setParticipants(String participants) { Participants = participants; }

    public String getWeddingDate() { return WeddingDate; }
    public void setWeddingDate(String weddingDate) { WeddingDate = weddingDate; }

    public Blob getImage() { return Image; }
    public void setImage(Blob image) { Image = image; }

}