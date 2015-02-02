/*
 * Copyright 2015 Higher Frequency Trading
 *
 *  http://www.higherfrequencytrading.com
 *  
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *  
 *  http://www.apache.org/licenses/LICENSE-2.0
 *  
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package net.openhft.chronicle.hash.impl.stage.entry;

import net.openhft.chronicle.hash.impl.VanillaChronicleHashHolder;
import net.openhft.sg.StageRef;
import net.openhft.sg.Staged;

@Staged
public class AllocatedChunks {

    @StageRef public VanillaChronicleHashHolder<?, ?, ?> hh;
    @StageRef public SegmentStages s;
    @StageRef public HashEntryStages<?> entry;

    public int allocatedChunks = 0;
    
    public void initAllocatedChunks(int allocatedChunks) {
        this.allocatedChunks = allocatedChunks;
    }
    
    public void incrementSegmentEntriesIfNeeded() {
        // don't increment
    }

    public void initEntryAndKeyCopying(long entrySize, long bytesToCopy) {
        initAllocatedChunks(hh.h().inChunks(entrySize));
        // call incrementSegmentEntriesIfNeeded() before entry.copyExistingEntry(), because
        // the latter clears out searchState, and it performs the search again, but in inconsistent
        // state
        incrementSegmentEntriesIfNeeded();
        entry.copyExistingEntry(s.alloc(allocatedChunks), bytesToCopy);
    }
}