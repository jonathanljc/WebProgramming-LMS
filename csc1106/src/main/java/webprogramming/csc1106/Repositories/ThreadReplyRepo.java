package webprogramming.csc1106.Repositories;

import org.springframework.data.repository.CrudRepository;

import webprogramming.csc1106.Entities.ThreadReply;


public interface ThreadReplyRepo extends CrudRepository<ThreadReply, Integer>{
    // Iterable<ThreadReply> findByThreadOrderByReplyDateDescReplyTimeDesc(int threadId);

    ThreadReply findByReplyID(int replyID);
}
