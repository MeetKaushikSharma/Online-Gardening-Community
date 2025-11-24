import { useEffect, useState } from 'react';
import { useStore } from '../../store/useStore';
import { useForm } from 'react-hook-form';
import { useAuth } from '../../context/AuthContext';

const Discussions = () => {
  const { user } = useAuth();
  const { discussions, fetchDiscussions, addDiscussion } = useStore();
  const [showForm, setShowForm] = useState(false);
  const { register, handleSubmit, reset } = useForm();

  useEffect(() => {
    fetchDiscussions().catch((error) => console.error('Failed to load discussions', error));
  }, [fetchDiscussions]);

  const onSubmit = async (data) => {
    if (!user?.id) {
      alert('Please log in again.');
      return;
    }

    try {
      await addDiscussion({
        topic: data.topic,
        content: data.content,
        userId: user.id
      });
      alert('Discussion posted successfully!');
      reset();
      setShowForm(false);
      fetchDiscussions();
    } catch (error) {
      alert(error.message || 'Unable to post discussion');
    }
  };

  return (
    <div>
      <div className="mb-4">
        <button
          onClick={() => setShowForm(!showForm)}
          className="btn btn-primary btn-accent-green px-4 py-2 rounded"
        >
          {showForm ? 'Cancel' : 'Start New Discussion'}
        </button>
      </div>

      {showForm && (
        <div className="glass-card p-6 mb-6">
          <h3 className="text-xl font-semibold mb-4 text-foreground">Start a New Discussion</h3>
          <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
            <div>
              <label className="form-label">Topic</label>
              <input
                {...register('topic', { required: true })}
                className="form-input"
                placeholder="Enter discussion topic"
              />
            </div>
            <div>
              <label className="form-label">Your Question/Comment</label>
              <textarea
                {...register('content', { required: true })}
                rows={4}
                className="form-input"
                placeholder="Share your thoughts or ask a question"
              />
            </div>
            <button
              type="submit"
              className="btn btn-primary btn-accent-green px-4 py-2 rounded"
            >
              Post Discussion
            </button>
          </form>
        </div>
      )}

      <div className="space-y-4">
        <h3 className="text-xl font-semibold text-foreground">Active Discussions</h3>
        {discussions.map((discussion) => {
          const createdDate = discussion.createdAt ? new Date(discussion.createdAt).toLocaleDateString() : 'Just now';
          return (
            <div key={discussion.id} className="glass-card p-6">
              <h4 className="text-lg font-semibold mb-2 text-foreground">{discussion.title}</h4>
              <p className="text-muted mb-4">{discussion.content}</p>
              <div className="flex justify-between items-center text-sm text-muted">
                <span>Posted by {discussion.authorName || 'Gardener'}</span>
                <span>{createdDate}</span>
              </div>
            </div>
          );
        })}
      </div>
    </div>
  );
};

export default Discussions;
